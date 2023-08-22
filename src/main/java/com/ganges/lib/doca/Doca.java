package com.ganges.lib.doca;

import com.ganges.lib.AnonymizationAlgorithm;
import com.ganges.lib.AnonymizationItem;
import com.ganges.lib.castleguard.utils.Utils;
import com.ganges.lib.doca.utils.DocaUtil;
import com.ganges.lib.doca.utils.GreenwaldKhannaQuantileEstimator;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import org.apache.commons.lang3.Range;
import org.apache.commons.math3.distribution.LaplaceDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;

public class Doca implements AnonymizationAlgorithm {

  //-----------Parameters/Variables for DELTA Phase----------------//
  private static GreenwaldKhannaQuantileEstimator GKQuantileEstimator =
      new GreenwaldKhannaQuantileEstimator(0.002);    // GK Quantile Estimator
  private static List<Double> Vinf = new ArrayList<>(); // List of all lower bounds for each header
  private static List<Double> Vsup = new ArrayList<>();  // List of all upper bounds for each header
  private final double LAMBDA = 0.8;      // tolerance parameter for domain building
  private final int beta;   // maximum number of clusters that can be stored
  private final int delayConstraint;  // maximum time a tuple can be stored before it's released
  private final double delta;   // suppression rate
  private boolean stableDomainReached = false;
  private static final int processingWindowSize = 50; //size of process window for domain bounding

  //-----------Parameters/Variables for DOCA Phase----------------//
  private List<DocaCluster> clusterList;  // List of all current active clusters
  private HashMap<String, Range<Double>> rangeMap = new HashMap<>(); // Global ranges
  private List<DocaItem> currentItems = new ArrayList<>();
  private final double eps; // privacy budget
  private List<Double> losses = new ArrayList<>();   // Losses to use when calculating tau
  private double tau; // average loss of last M expired clusters
  private List<DocaItem> domain = new ArrayList<>();
  private List<AnonymizationItem> leftOverItems = new ArrayList<>();
  private Map<DocaItem, Double> testDocaBuilding = new HashMap<>();



  public Doca() {
    String[] parameters = getParameters();
    this.eps = Double.parseDouble(parameters[0]);
    this.delayConstraint = Integer.parseInt(parameters[1]);
    this.beta = Integer.parseInt(parameters[2]);
    this.delta = Double.parseDouble(parameters[4]);
    this.domain = new ArrayList<>();
    this.tau = 0;
    this.clusterList = new ArrayList<>();
  }

  public static String[] getParameters() {
    String[] result = new String[5];
    String userDirectory = System.getProperty("user.dir");
    try (InputStream inputStream = Files.newInputStream(
        Paths.get(userDirectory + "/src/main/resources/doca.properties"))) {
      Properties properties = new Properties();
      properties.load(inputStream);
      result[0] = properties.getProperty("eps");
      result[1] = properties.getProperty("delay_constraint");
      result[2] = properties.getProperty("beta");
      result[3] = properties.getProperty("inplace");
      result[4] = properties.getProperty("delta");
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AnonymizationItem> anonymize(List<AnonymizationItem> x) {
    // Append Left over Items from previous batch (only used when domain bounding is used)
    x.addAll(0, leftOverItems);
    leftOverItems = new ArrayList<>();

    if (x.isEmpty() || x.get(0).getValues().isEmpty()) {
      return new ArrayList<>();
    }

    List<AnonymizationItem> outputResult = new ArrayList<>();
    for (AnonymizationItem docaInput : x) {
      DocaItem currentItem = new DocaItem(docaInput.getId(), docaInput.getValues(), docaInput.getNonAnonymizedValues(),
          docaInput.getValues().keySet().stream().toList());

      // If Delta is 1 no suppression (Domain Bounding) is applied
      if (this.delta != 1) {
        this.stableDomainReached = this.addToDomain(currentItem);

        // If stable domain is reached
        if (this.stableDomainReached) {
          this.leftOverItems.addAll(x.subList(x.indexOf(docaInput), x.size()));
          break;
        }
      } else {
        this.domain.add(currentItem);
      }
    }

    if (!this.stableDomainReached && this.delta != 1) {
      return new ArrayList<>();
    }

    Map<String, Double> maximums = DocaUtil.getMax(this.domain);
    Map<String, Double> minimums = DocaUtil.getMin(this.domain);
    // TODO: Make Global Sensitivity List
    Map<String, Double> sensitivityList = new HashMap<>();
    for (String header : maximums.keySet()) {
      if (Objects.equals(maximums.get(header), minimums.get(header))) {
        sensitivityList.put(header, 1.0);
      } else {
        sensitivityList.put(header, Math.abs(minimums.get(header) - maximums.get(header)));
      }
    }

    for (DocaItem docaItem : this.domain) {
      List<AnonymizationItem> result = this.addData(docaItem, sensitivityList);
      outputResult.addAll(result);
    }
    this.domain = new ArrayList<>();
    return outputResult;
  }


  /**
   * Added Tuple gets either suppressed or released to the DOCA Phase.
   *
   * @param currentItem Input Tuple
   */
  public List<AnonymizationItem> addData(DocaItem currentItem, Map<String, Double> sensitivityList) {
    //TODO: remove addData -> doca function is sufficient
    List<DocaItem> anonymizedData = new ArrayList<>();
    List<DocaItem> returnedItems = this.doca(currentItem, sensitivityList);
    if (!returnedItems.isEmpty()) {
      anonymizedData.addAll(returnedItems);
    }
    return anonymizedData.stream().map(anonItem ->
        new AnonymizationItem(anonItem.getExternalId(), anonItem.getData(),
            anonItem.getNonAnonymizedData())).toList();
  }

  /**
   * Adds a Tuple to the domain and checks if it is stable.
   * if domain is stable, it will be released
   *
   * @param x Tuple to be added
   * @return the domain if it is stable, otherwise null
   */
  protected Boolean addToDomain(DocaItem x) {
    double tolerance = this.LAMBDA;

    // EXPERIMENTAL: Use mean of tuple as value for GK
    // Get mean value of data point
    double sum = x.getData().values().stream().reduce(0.0, Double::sum);
    double mean = sum / x.getHeaders().size();

    // Add tuple to GKQEstimator
    GKQuantileEstimator.add(mean, x);
    testDocaBuilding.put(x, mean);

    // Add Quantile to Vinf and Vsup
    // Key is index, value is value
    HashMap<DocaItem, Double> estQuantileInf =
        GKQuantileEstimator.getQuantile((1.0 - this.delta) / 2.0);
    HashMap<DocaItem, Double> estQuantileSup =
        GKQuantileEstimator.getQuantile((1.0 + this.delta) / 2.0);

    Vinf.add((double) estQuantileInf.values().toArray()[0]);
    Vsup.add((double) estQuantileSup.values().toArray()[0]);

    // Check if processing window is large enough
    if (Vinf.size() == processingWindowSize && Vsup.size() == processingWindowSize) {
      double stdVinf = DocaUtil.calculateStandardDeviation(Vinf);
      double stdVsup = DocaUtil.calculateStandardDeviation(Vsup);

      double meanVinf = Vinf.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
      double meanVsup = Vsup.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

      //coefficient of variation
      double cvVinf = stdVinf / meanVinf;
      double cvVsup = stdVsup / meanVsup;

      // check if domain is stable
      if (cvVinf < tolerance && cvVsup < tolerance) {

        DocaItem from = (DocaItem) estQuantileInf.keySet().toArray()[0];
        DocaItem to = (DocaItem) estQuantileSup.keySet().toArray()[0];

        List<DocaItem> sortedItems = testDocaBuilding.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .toList();
        this.domain = sortedItems.subList(sortedItems.indexOf(from), sortedItems.indexOf(to) + 1);

        Vinf = new ArrayList<>();
        Vsup = new ArrayList<>();
        GKQuantileEstimator = new GreenwaldKhannaQuantileEstimator(0.02);

        // Reset Doca Stage when using StableDomains
        this.clusterList.clear();
        this.rangeMap.clear();
        this.tau = 0;

        return true;
      }
      // remove oldest tuple to remain size of stable domain
      Vinf.remove(0);
      Vsup.remove(0);
    }
    return false;
  }


  /**
   * Adds a Tuple to the domain and checks if it is stable.
   * if domain is stable, it will be released
   *
   * @param dataTuple     Tuple to be added
   * @param sensitivities sensitivity of the tuple
   * @return the domain if it is stable, otherwise null
   */
  protected List<DocaItem> doca(DocaItem dataTuple, Map<String, Double> sensitivities) {
    // Add tuple to best cluster and return expired clusters if any
    DocaCluster expiringCluster = this.onlineClustering(dataTuple);

    List<DocaItem> releasedItems = new ArrayList<>();
    // Release Cluster if expired
    if (expiringCluster != null) {
      releasedItems.addAll(releaseExpiredCluster(expiringCluster, sensitivities));
    }
    return releasedItems;
  }

  /**
   * Online Clustering Algorithm.
   *
   * @param tuple Data Tuple to be clustered
   * @return List of expired clusters, the list is empty if no cluster expired
   */
  private DocaCluster onlineClustering(DocaItem tuple) {

    DocaCluster bestCluster = this.findBestCluster(tuple, DocaUtil.getAttributeDiff(this.rangeMap));

    if (bestCluster == null) {
      DocaCluster newCluster = new DocaCluster(tuple.getHeaders());
      newCluster.insert(tuple);
      this.clusterList.add(newCluster);
    } else {
      bestCluster.insert(tuple);
    }

    // update global ranges
    for (String header : tuple.getHeaders()) {
      if (this.rangeMap.containsKey(header)) {
        Utils.updateDoubleRange(this.rangeMap.get(header), tuple.getData().get(header));
      } else {
        this.rangeMap.put(header, Range.is(tuple.getData().get(header)));
      }
    }

    DocaCluster expiredCluster;
    // add tuple to currently active items
    if (this.currentItems.size() <= this.delayConstraint) {
      this.currentItems.add(tuple);
      return null;
    } else {
      DocaItem expiredTuple = this.currentItems.remove(0);
      this.currentItems.add(tuple);
      expiredCluster = expiredTuple.getCluster();
    }
    return expiredCluster;
  }

  /**
   * Find the best cluster for a given data point.
   *
   * @param dataPoint Data point to be clustered
   * @param dif       Difference of global ranges
   * @return Best cluster for the data point or null if no fitting cluster was found
   */
  private DocaCluster findBestCluster(DocaItem dataPoint, HashMap<String, Double> dif) {
    int bestCluster = -1;
    int numAttributes = dataPoint.getHeaders().size();
    List<DocaCluster> clusters = this.clusterList;

    // Calculate enlargement (the value is not yet divided by the number of attributes!)
    List<Double> enlargement = new ArrayList<>();
    for (DocaCluster cluster : clusters) {
      double sum = 0;
      for (Map.Entry<String, Double> entry : dataPoint.getData().entrySet()) {
        String key = entry.getKey();
        Double value = entry.getValue();
        sum += Math.max(0, value - cluster.getRanges().get(key).getMaximum())
            - Math.min(0, value - cluster.getRanges().get(key).getMinimum());
      }
      enlargement.add(sum);
    }

    // Find minimum enlargement
    double minEnlarge;
    if (enlargement.isEmpty()) {
      minEnlarge = Double.POSITIVE_INFINITY;
    } else {
      minEnlarge = enlargement.stream().min(Double::compare).get();
    }

    // Find clusters with minimum enlargement
    // and Find acceptable clusters (with overall loss <= tau)
    List<Integer> okClusters = new ArrayList<>();
    List<Integer> minClusters = new ArrayList<>();
    for (int c = 0; c < clusters.size(); c++) {
      double enl = enlargement.get(c);
      if (enl == minEnlarge) {
        minClusters.add(c);

        HashMap<String, Double> difCluster = new HashMap<>();
        for (Map.Entry<String, Range<Double>> clusterRange : clusters.get(c).getRanges()
            .entrySet()) {
          difCluster.put(clusterRange.getKey(),
              clusterRange.getValue().getMaximum() - clusterRange.getValue().getMinimum());
        }
        double overallLoss = (enl
            + DocaUtil.divisionWith0(difCluster, dif).values().stream().reduce(0.0, Double::sum)
            / numAttributes);
        if (overallLoss <= this.tau) {
          okClusters.add(c);
        }
      }
    }
    // First try to find a cluster with minimum enlargement and acceptable loss
    if (!okClusters.isEmpty()) {
      bestCluster = okClusters.stream()
          .min(Comparator.comparingInt(c -> clusters.get(c).getContents().size()))
          .orElse(-1);
      // If no new cluster is allowed, try to find a cluster with minimum enlargement
    } else if (clusters.size() >= beta) {
      bestCluster = minClusters.stream()
          .min(Comparator.comparingInt(c -> clusters.get(c).getContents().size()))
          .orElse(-1);
    }

    if (bestCluster == -1) {
      return null;
    } else {
      return clusters.get(bestCluster);
    }
  }

  /**
   * Release an expired cluster after perturbation.
   *
   * @param expiredCluster  Cluster to be released
   * @param sensitivityList Sensitivity for the perturbation of each header
   * @return an expired cluster or empty list if no cluster was released (e.g. delay constraint not
   *        reached)
   */
  private List<DocaItem> releaseExpiredCluster(DocaCluster expiredCluster,
                                               Map<String, Double> sensitivityList) {
    // update values
    HashMap<String, Double> dif = DocaUtil.getAttributeDiff(this.rangeMap);
    HashMap<String, Double> difCluster = new HashMap<>();
    for (Map.Entry<String, Range<Double>> clusterRange : expiredCluster.getRanges().entrySet()) {
      difCluster.put(clusterRange.getKey(),
          clusterRange.getValue().getMaximum() - clusterRange.getValue().getMinimum());
    }
    double loss =
        DocaUtil.divisionWith0(difCluster, dif).values().stream().reduce(0.0, Double::sum)
            / (float) difCluster.keySet().size();
    this.losses.add(loss);
    //TODO: tau should only be calculated from the last m losses
    this.tau = this.losses.stream().mapToDouble(Double::doubleValue).sum() / losses.size();
    // release cluster from list
    this.clusterList.remove(expiredCluster);

    // perturbs cluster items
    HashMap<String, Double> attr = new HashMap<>();
    for (DocaItem item : expiredCluster.getContents()) {
      // Sum up each Attribute
      for (String header : item.getHeaders()) {
        if (!attr.containsKey(header)) {
          attr.put(header, 0.0);
        }
        attr.put(header, attr.get(header) + item.getData().get(header));
      }
    }
    // Calculate mean of Attributes
    HashMap<String, Double> mean = new HashMap<>();
    for (Map.Entry<String, Double> attrEntry : attr.entrySet()) {
      mean.put(attrEntry.getKey(), attrEntry.getValue() / expiredCluster.getContents().size());
    }

    Map<String, Double> noise = getNoise(expiredCluster, sensitivityList, mean);

    for (DocaItem i : expiredCluster.getContents()) {
      for (Map.Entry<String, Double> entry : i.getData().entrySet()) {
        entry.setValue(mean.get(entry.getKey()));
      }
    }

    for (DocaItem expired : expiredCluster.getContents()) {
      this.currentItems.removeIf(
          currentItem -> expired.getExternalId().equals(currentItem.getExternalId()));
    }
    expiredCluster.pertubeCluster(noise);
    return expiredCluster.getContents();
  }

  private Map<String, Double> getNoise(DocaCluster expiredCluster,
                                       Map<String, Double> sensitivityMap,
                                       HashMap<String, Double> mean) {
    HashMap<String, Double> laplaceNoise = new HashMap<>();
    for (String header : mean.keySet()) {

      double deltaC = sensitivityMap.get(header) / (expiredCluster.getContents().size());
      double scale = deltaC / eps;

      JDKRandomGenerator rg = new JDKRandomGenerator();
      LaplaceDistribution laplaceDistribution = new LaplaceDistribution(rg, 0, scale);
      Double noise = laplaceDistribution.sample();
      laplaceNoise.put(header, noise);
    }

    return laplaceNoise;
  }

  private void updateSensitivities() {
    //TODO
  }

}
