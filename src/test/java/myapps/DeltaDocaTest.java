package myapps;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;


public class DeltaDocaTest {

    double[] normalHeader1 = null;
    double[] normalHeader2 = null;
    double[] randomHeader1 = null;
    double[] randomHeader2 = null;
    Doca doca = null;

    @Before
    public void setUp() {

        this.normalHeader1 = new double[]{50.56, 78.39, 60.63, 71.47, 13.12, 35.81, 50.99, 51.61, 68.5, 51.93, 85.24, 66.53, 51.94, 71.08, 33.08, 61.08, 57.52, 58.08, 64.8, 57.45, 29.14, 30.31, 75.45, 47.78, 52.28, 32.93, 44.01, 44.87, 71.52, 35.49, 71.23, 70.21, 85.16, 29.95, 27.0, 67.97, 11.65, 81.52, 23.35, 67.11, 23.38, 54.21, 40.01, 35.64, 55.09, 69.35, 13.08, 79.35, 61.1, 51.34, 34.76, 66.29, 40.59, 44.44, 67.78, 22.84, 42.74, 77.51, 29.96, 57.71, 40.82, 55.01, 35.7, 57.97, 46.98, 84.9, 62.15, 36.88, 34.9, 48.23, 25.22, 31.43, 23.13, 43.03, 34.25, 32.41, 40.81, 40.05, 16.47, 70.37, 52.51, 49.11, 40.88, 22.53, 39.11, 1.708, 56.85, 33.94, 30.36, 62.59, 51.45, 66.83, 50.46, 35.18, 45.34, 57.76, 49.05, 35.81, 58.38, 18.39, 43.75, 24.99, 25.77, 53.77, 18.9, 64.87, 41.66, 43.41, 63.63, 68.88, 86.92, 16.18, 29.36, 32.43, 47.22, 54.04, 45.01, 76.05, 49.44, 50.53, 16.98, 37.04, 18.1, 66.68, 11.11, 66.01, 33.78, 56.59, 43.03, 64.63, 30.76, 47.27, 53.62, 57.82, 77.99, 34.95, 79.66, 28.12, 46.19, 18.03, 61.93, 79.48, 52.13, 61.53, 46.56, 33.3, 24.98, 64.49, 57.12, 50.51, 76.79, 22.65, 56.33, 23.14, 61.04, 64.33, 27.02, 49.67, 38.96, 54.48, 20.74, 31.63, 53.86, 84.84, 3.085, 68.42, 57.27, 70.23, 68.15, 33.4, 44.73, 41.9, 27.39, 19.85, 56.9, 87.69, 50.85, 28.47, 57.09, 31.35, 86.72, 78.87, 56.03, 31.19, 60.05, 60.26, 55.31, 51.33, 78.96, 35.89, 58.0, 74.44, 61.03, 59.81, 37.18, 56.33, 98.87, 52.13, 60.44, 53.12, 54.74, 13.8, 89.85, 50.97, 42.15, 70.22, 56.18, 70.03, 52.78, 50.32, 64.02, 44.75, 52.01, 55.41, 61.02, 50.64, 53.02, 60.13, 52.71, 65.26, 29.26, 56.84, 57.99, 20.62, 53.17, 37.12, 67.15, 55.65, 15.53, 19.79, 38.88, 59.71, 48.4, 37.34, 28.43, 22.44, 53.63, 35.97, 47.01, 46.22, 79.79, 30.29, 33.57, 85.84, 70.63, 23.05, 80.7, 56.78, 51.71, 54.66, 35.95, 66.86, 24.15, 53.41, 34.4, 38.28, 53.35, 94.09, 46.64, 41.51, 64.35, 90.88, 42.69, 50.29, 79.47, 40.0, 71.77, 57.91, 62.89, 56.44, 80.8, 35.76, 67.63, 46.87, 44.12, 50.69, 53.8, 52.88, 56.47, 29.33, 25.29, 83.95, 80.93, 65.08, 55.1, 36.7, 42.2, 51.55, 66.98, 8.529, 43.34, 54.78, 49.29, 31.0, 51.8, 60.78, 69.47, 26.34, 63.84, 62.7,
                42.81, 64.06, 61.16, 61.54, 60.8, 49.32, 18.92, 10.67, 34.11, 54.31, 45.57, 32.39, 65.84, 69.07, 39.47, 65.12, 73.43, 39.97, 71.7, 44.1, 57.37, 54.79, 62.0, 40.97, 50.51, 26.71, 39.26, 48.2, 38.92, 26.19, 47.86, 61.5, 61.31, 38.78, 67.15, 47.64, 64.76, 32.44, 53.65, 57.28, 52.36, 50.93, 37.77, 46.76, 35.21, 68.7, 28.14, 64.03, 53.4, 57.33, 30.92, 38.98, 29.39, 34.06, 55.46, 73.5, 39.22, 55.9, 45.07, 43.67, 44.17, 54.49, 53.42, 39.95, 42.4, 71.14, 66.02, 54.06, 43.56, 47.05, 46.01, 45.96, 24.81, 41.8, 67.93, 19.84, 43.76, 66.05, 50.33, 58.72, 56.28, 41.39, 62.58, 34.44, 48.38, 55.61, 49.83, 38.04, 32.59, 36.18, 64.91, 48.12, 77.32, 62.29, 49.68, 55.54, 69.5, 21.04, 48.36, 27.34, 53.53, 76.49, 77.85, 41.41, 56.51, 45.34, 71.91, 48.47, 55.54, 32.95, 47.47, 56.03, 45.33, 68.05, 33.0, 73.26, 4.261, 57.67, 38.39, 34.64, 44.35, 36.66, 52.21, 49.5, 67.27, 67.47, 72.51, 80.29, 65.62, 24.33, 42.61, 44.85, 62.07, 22.8, 41.06, 41.74, 25.86, 46.83, 51.8, 52.73, 46.65, 70.66, 66.34, 55.12, 59.09, 56.2, 49.83, 69.05, 96.24, 68.91, 50.72, 63.41, 47.35, 27.99, 67.98, 52.63, 77.49, 46.54, 61.37, 39.98, 45.61, 60.16, 61.28, 31.18, 37.9, 45.12, 75.06, 12.29, 68.46, 63.38, 72.71, 35.14, 54.85, 49.52, 59.09, 61.27, 62.62, 48.09, 44.51, 32.24, 31.34, 52.44, 37.44, 36.22, 44.34, 36.23, 14.81, 64.8, 52.23, 46.83, 67.53, 40.68, 23.84, 45.88, 49.78, 57.03, 37.87, 38.66, 53.78, 40.96, 41.48, 42.29, 63.7, 34.02, 37.04, 20.45, 49.5, 71.73, 39.82, 27.32, 30.28, 33.58, 42.04, 38.38, 51.01, 35.16, 54.44, 51.0, 41.73, 43.82, 65.06, 31.23, 41.01, 50.98, 51.82, 40.92, 43.9, 58.16, 68.25, 65.6, 32.56, 59.67, 63.66, 50.1, 52.01, 31.21, 43.8, 47.99, 49.68, 44.32, 43.37, 41.32, 75.04, 56.99, 49.46, 50.65, 60.1, 56.59, 57.97, 46.35, 43.27, 41.55, 51.43, 57.24, 56.08, 43.66, 29.44, 59.81, 19.69, 63.34, 37.28, 67.81, 19.94, 28.27, 28.92, 45.02, 46.18, 48.47, 44.83, 55.43, 65.35, 38.79, 39.69, 53.62, 32.79, 57.65, 55.82, 49.54, 83.33, 47.22, 60.03, 61.53, 53.56, 56.34, 58.54, 44.05, 49.32, 52.33, 72.32, 53.99, 49.47, 62.62, 54.45, 69.43, 73.05, 62.5, 45.99, 63.47, 31.8, 57.95};
        this.normalHeader2 = new double []{12.21, 13.95, 17.85, 15.88, 12.47, 13.75, 14.07, 13.34, 10.52, 11.19, 16.64, 16.22, 11.69, 11.8, 14.25, 16.69, 15.35, 11.82, 13.26, 11.94, 14.51, 17.55, 17.13, 16.79, 16.24, 19.0, 14.55, 12.86, 12.76, 12.5, 17.89, 11.5, 14.34, 18.17, 14.23, 14.42, 17.44, 14.8, 13.75, 12.18, 13.6, 20.82, 14.35, 8.758, 9.23, 9.9, 12.11, 13.17, 14.35, 17.41, 8.144, 8.513, 10.63, 18.82, 20.71, 13.18, 14.77, 19.26, 13.99, 20.42, 11.5, 14.92, 16.53, 17.06, 10.94, 16.52, 19.4, 20.0, 10.29, 18.83, 10.99, 17.35, 10.18, 14.65, 15.51, 14.61, 14.55, 18.48, 15.46, 13.04, 11.74, 16.75, 14.73, 19.63, 21.81, 9.753, 9.671, 18.12, 16.17, 13.39, 14.6, 15.77, 13.5, 14.45, 17.38, 12.79, 13.53, 19.47, 19.63, 21.78, 11.68, 14.99, 13.8, 18.07, 12.16, 16.82, 13.41, 14.39, 15.49, 15.04, 14.41, 15.8, 11.66, 13.45, 17.31, 20.87, 11.22, 18.66, 12.54, 14.6, 15.16, 10.58, 9.179, 15.87, 16.38, 8.629, 12.51, 12.48, 15.68, 14.72, 18.6, 24.41, 12.4, 9.056, 21.4, 17.67, 11.97, 22.15, 16.03, 14.15, 14.97, 20.16, 16.06, 17.14, 17.5, 15.61, 16.07, 15.9, 14.3, 12.82, 16.12, 13.52, 15.43, 12.51, 13.96, 14.86, 17.85, 13.37, 15.72, 14.15, 17.32, 10.89, 17.49, 15.5, 17.45, 14.45, 17.64, 18.44, 14.97, 17.73, 17.9, 14.56, 12.92, 13.63, 15.28, 13.52, 12.16, 16.41, 11.76, 12.31, 17.23, 13.83, 18.26, 19.54, 10.42, 12.15, 17.46, 15.15, 13.62, 14.03, 15.74, 16.06, 7.766, 14.77, 14.02, 9.843, 12.72, 15.38, 14.42, 16.18, 9.361, 12.51, 18.38, 13.13, 17.69, 14.11, 12.39, 10.76, 16.92, 11.78, 16.48, 12.35, 16.86, 12.05, 17.78, 15.99, 12.89, 17.55, 13.18, 18.07, 14.6, 17.35, 13.96, 12.11, 15.15, 13.68, 10.32, 13.71, 12.89, 13.22, 15.32, 14.34, 12.57, 16.32, 9.48, 13.77, 16.15, 17.2, 12.42, 10.78, 13.01, 17.38, 14.19, 18.72, 16.72, 14.47, 12.44, 13.4, 14.13, 18.45, 14.02, 9.57, 15.18, 16.0, 19.02, 17.48, 14.69, 15.97, 17.81, 10.98, 12.2, 16.83, 9.756, 18.97, 9.785, 12.72, 15.51, 16.45, 13.48, 22.93, 13.99, 17.53, 15.03, 13.56, 19.19, 13.79, 17.32, 19.74, 15.96, 12.5, 15.63, 15.54, 13.16, 14.74, 14.56, 16.84, 13.81, 18.97, 19.17, 15.64, 11.08, 20.94, 14.71, 15.2, 20.42, 13.71, 18.36, 14.38, 15.67, 18.65,
                16.58, 21.13, 19.09, 16.52, 24.24, 25.69, 15.2, 21.94, 17.82, 19.08, 20.26, 25.38, 18.14, 21.84, 20.62, 14.89, 16.26, 16.86, 17.21, 15.63, 18.63, 19.13, 20.43, 16.49, 24.82, 15.26, 13.18, 23.52, 22.81, 20.67, 13.9, 18.88, 24.35, 26.15, 17.82, 21.49, 19.32, 19.41, 19.88, 12.52, 14.07, 27.54, 19.27, 18.17, 19.86, 16.3, 17.54, 17.17, 18.22, 18.32, 30.25, 28.2, 21.09, 19.19, 23.86, 22.02, 22.51, 13.1, 15.18, 20.38, 11.45, 18.58, 16.43, 24.73, 24.77, 17.16, 25.26, 19.1, 13.91, 17.77, 15.35, 14.33, 22.91, 18.71, 20.22, 28.16, 23.23, 16.63, 15.35, 14.32, 28.68, 26.17, 15.65, 15.36, 22.31, 19.57, 15.54, 25.42, 18.84, 22.12, 17.64, 19.85, 26.74, 18.71, 19.07, 28.13, 12.27, 20.9, 23.61, 24.31, 20.46, 17.95, 21.15, 21.39, 23.04, 18.55, 13.71, 22.61, 22.17, 18.42, 17.97, 16.71, 17.08, 29.18, 18.19, 14.49, 17.56, 14.53, 20.93, 17.1, 27.68, 25.19, 20.79, 26.99, 14.84, 15.22, 24.84, 23.77, 14.74, 11.14, 17.7, 16.73, 12.48, 24.66, 21.13, 24.3, 27.23, 16.09, 24.92, 24.4, 22.48, 14.28, 19.44, 20.29, 15.17, 22.0, 18.07, 22.43, 21.45, 20.66, 20.65, 22.73, 18.76, 22.06, 17.32, 18.85, 24.79, 15.48, 18.2, 17.64, 20.76, 21.28, 23.29, 22.64, 19.63, 30.97, 27.12, 16.65, 15.82, 27.47, 16.55, 20.78, 18.56, 20.75, 15.96, 15.37, 21.69, 19.46, 23.12, 18.22, 22.06, 23.66, 22.17, 16.69, 17.73, 14.06, 23.53, 21.55, 22.08, 16.58, 19.7, 17.2, 18.11, 23.12, 21.63, 15.17, 11.38, 17.6, 24.92, 24.47, 13.45, 20.81, 16.7, 22.92, 25.34, 23.88, 15.89, 16.73, 25.35, 22.26, 19.21, 17.74, 14.63, 18.76, 25.22, 18.63, 21.48, 18.9, 15.38, 13.99, 20.56, 15.72, 22.91, 23.18, 27.59, 25.81, 24.98, 17.33, 24.12, 20.94, 19.74, 21.17, 27.31, 23.85, 18.39, 23.08, 23.96, 20.2, 18.27, 17.21, 24.95, 17.35, 17.35, 24.46, 23.38, 20.35, 23.42, 16.01, 20.15, 17.59, 19.01, 20.47, 15.04, 20.42, 23.73, 23.54, 27.87, 22.49, 25.5, 21.54, 17.14, 20.6, 15.56, 12.92, 14.35, 23.97, 24.47, 20.8, 17.99, 19.88, 21.28, 13.06, 21.77, 19.74, 15.04, 13.64, 17.27, 21.6, 20.26, 18.5, 14.29, 16.0, 18.23, 15.92, 19.86, 25.35, 21.63, 25.0, 19.82, 24.23, 21.05, 13.93, 15.42, 26.28, 30.6, 19.57, 19.25, 19.63, 15.62, 11.08};

        Random rand = new Random();

        for (int i = 0; i < this.normalHeader1.length; i++) {
            int randomIndexToSwap = rand.nextInt(this.normalHeader1.length);
            double temp1 = this.normalHeader1[randomIndexToSwap];
            this.normalHeader1[randomIndexToSwap] = this.normalHeader1[i];
            this.normalHeader1[i] = temp1;

            double temp2 = this.normalHeader2[randomIndexToSwap];
            this.normalHeader2[randomIndexToSwap] = this.normalHeader2[i];
            this.normalHeader2[i] = temp2;
        }

        this.randomHeader1 = new double[]{29.77, 2.125, 13.37, 11.04, 34.14, 31.45, 41.14, 4.912, 19.98, 2.34, 10.83, 23.74, 2.194, 9.947, 30.24, 25.52, 10.91, 27.51, 37.42, 1.292, 37.26, 32.41, 16.31, 7.996, 44.07, 16.14, 5.173, 5.352, 39.13, 28.16, 37.32, 33.83, 25.13, 44.79, 18.03, 25.84, 38.32, 28.83, 39.77, 26.98, 32.7, 3.062, 11.25, 14.02, 4.59, 11.47, 5.545, 13.5, 29.6, 17.41, 17.65, 10.42, 13.01, 43.14, 30.16, 28.41, 8.701, 33.81, 8.353, 18.07, 45.52, 29.79, 26.06, 31.8, 38.92, 35.91, 11.3, 2.444, 15.19, 13.04, 10.49, 43.43, 40.43, 15.16, 30.49, 18.8, 42.15, 21.64, 12.91, 12.09, 26.26, 12.82, 27.3, 41.4, 18.97, 10.86, 45.88, 23.92, 5.09, 3.12, 5.934, 29.23, 36.64, 19.99, 3.858, 18.17, 45.82, 24.81, 44.69, 39.73, 1.516, 33.43, 31.67, 25.16, 13.0, 29.84, 6.019, 20.56, 21.41, 43.92, 40.41, 12.85, 23.52, 9.039, 42.06, 40.17, 14.43, 29.75, 28.4, 7.877, 35.31, 25.27, 36.03, 24.86, 1.025, 15.58, 1.876, 42.8, 40.54, 38.42, 14.83, 3.606, 40.51, 43.61, 4.854, 22.86, 4.114, 35.22, 35.46, 6.777, 22.38, 25.74, 12.92, 40.25, 20.04, 10.53, 25.26, 33.84, 10.05, 15.02, 45.78, 30.24, 20.71, 24.29, 6.445, 11.11, 16.21, 27.47, 11.35, 10.9, 4.194, 29.39, 11.3, 41.74, 39.68, 4.188, 11.71, 31.1, 10.64, 6.954, 43.09, 26.69, 22.27, 36.3, 37.33, 9.568, 5.361, 20.39, 20.06, 22.01, 33.8, 31.3, 45.28, 5.428, 19.11, 16.26, 39.77, 12.18, 9.559, 21.18, 19.98, 13.53, 12.24, 42.54, 20.94, 39.76, 25.76, 3.276, 45.96, 38.62, 44.6, 42.68, 39.19, 8.483, 22.85, 10.61, 19.04, 3.638, 18.05, 45.33, 12.93, 36.28, 21.47, 20.03, 44.07, 45.79, 26.0, 33.32, 7.965, 14.35, 44.59, 27.06, 25.39, 34.65, 3.572, 27.28, 23.62, 39.37, 8.084, 44.23, 4.605, 9.362, 27.77, 31.38, 11.58, 6.394, 41.06, 12.07, 27.75, 28.87, 19.86, 27.26, 24.52, 43.06, 10.19, 33.22, 11.74, 18.81, 31.22, 14.49, 15.22, 34.83, 4.264, 21.62, 45.93, 45.82, 4.296, 10.59, 12.93, 42.99, 40.63, 40.56, 17.62, 8.098, 38.51, 32.65, 28.52, 45.42, 30.42, 1.352, 37.76, 14.47, 30.85, 43.25, 7.043, 6.194, 5.816, 25.89, 13.25, 28.21, 33.29, 10.16, 29.54, 12.87, 22.98, 41.74, 39.07, 5.153, 20.06, 13.45, 1.159, 35.7, 29.67, 12.78, 34.35, 25.82, 20.24, 1.435, 4.385, 40.73, 41.67, 25.55, 38.55, 27.21, 7.664, 6.735, 14.87, 41.45, 36.82, 39.73, 41.45, 10.45, 12.22, 5.625,
                36.1, 40.78, 19.28, 28.92, 7.954, 42.84, 39.9, 44.92, 37.48, 40.66, 2.115, 34.14, 15.94, 42.88, 37.1, 39.88, 37.48, 13.0, 36.43, 5.864, 40.24, 39.63, 11.0, 37.74, 21.71, 14.73, 36.79, 11.24, 2.064, 9.69, 15.77, 39.89, 44.51, 13.56, 29.86, 18.98, 45.15, 25.12, 43.26, 6.19, 44.66, 9.035, 44.31, 12.94, 5.878, 20.55, 33.78, 15.11, 28.27, 24.01, 18.33, 26.94, 12.46, 32.89, 1.076, 42.65, 25.23, 33.37, 34.38, 31.17, 17.38, 4.148, 30.89, 15.85, 15.12, 39.16, 33.38, 14.51, 14.91, 19.37, 19.1, 14.3, 6.727, 19.92, 43.31, 31.47, 41.62, 28.69, 14.54, 25.65, 1.018, 13.91, 20.34, 27.09, 30.46, 21.92, 20.89, 10.61, 22.29, 41.55, 36.82, 8.636, 4.815, 24.19, 29.48, 16.08, 37.82, 34.8, 31.27, 11.1, 9.96, 2.099, 12.01, 22.38, 39.23, 4.277, 19.64, 29.33, 9.749, 32.33, 23.24, 11.97, 30.52, 1.249, 34.79, 35.65, 5.796, 20.13, 8.914, 44.1, 24.3, 3.259, 12.21, 39.17, 21.54, 37.06, 31.04, 45.45, 27.79, 43.75, 41.11, 28.56, 33.36, 23.71, 38.37, 25.65, 41.37, 34.46, 22.36, 12.66, 12.12, 29.69, 35.46, 24.45, 29.2, 13.35, 4.486, 13.85, 13.22, 15.38, 25.3, 7.226, 11.4, 32.22, 32.78, 3.89, 19.34, 25.41, 19.7, 10.3, 19.9, 41.71, 27.28, 32.29, 39.55, 35.45, 18.11, 1.265, 16.82, 34.9, 39.4, 43.9, 19.85, 34.63, 25.57, 28.14, 10.92, 10.87, 20.61, 2.306, 16.12, 31.56, 19.19, 8.427, 22.03, 6.743, 29.0, 2.213, 18.73, 26.39, 2.219, 29.92, 7.106, 21.77, 3.262, 18.05, 10.52, 15.7, 35.25, 18.06, 34.84, 38.43, 12.35, 4.685, 1.872, 25.27, 45.99, 16.74, 30.25, 36.15, 30.32, 34.94, 43.73, 9.971, 1.917, 7.857, 6.679, 31.12, 26.37, 10.8, 32.47, 35.51, 8.55, 28.32, 34.65, 6.153, 37.86, 44.41, 5.864, 2.155, 15.03, 31.48, 44.11, 18.84, 33.17, 4.419, 32.07, 29.22, 5.585, 35.76, 39.26, 28.01, 6.447, 45.27, 36.21, 16.62, 20.27, 17.67, 23.76, 16.35, 39.23, 38.0, 5.749, 44.23, 29.6, 38.29, 32.82, 20.59, 34.02, 44.44, 13.15, 37.36, 25.21, 22.75, 20.6, 33.89, 13.07, 39.32, 38.38, 4.899, 40.67, 11.97, 21.91, 28.46, 18.05, 2.291, 39.29, 9.182, 10.54, 36.9, 16.31, 40.61, 32.55, 13.43, 1.456, 43.66};
        this.randomHeader2 = new double[]{612.7, 600.5, 605.5, 604.4, 614.7, 613.5, 617.8, 601.7, 608.4, 600.5, 604.3, 610.1, 600.5, 603.9, 612.9, 610.8, 604.4, 611.7, 616.1, 600.1, 616.1, 613.9, 606.8, 603.1, 619.1, 606.7, 601.8, 601.9, 616.9, 612.0, 616.1, 614.5, 610.7, 619.4, 607.5, 611.0, 616.5, 612.3, 617.2, 611.5, 614.0, 600.9, 604.5, 605.7, 601.5, 604.6, 602.0, 605.5, 612.7, 607.2, 607.4, 604.1, 605.3, 618.7, 612.9, 612.1, 603.4, 614.5, 603.2, 607.5, 619.7, 612.7, 611.1, 613.6, 616.8, 615.5, 604.5, 600.6, 606.3, 605.3, 604.2, 618.8, 617.5, 606.2, 613.1, 607.9, 618.2, 609.1, 605.2, 604.9, 611.2, 605.2, 611.6, 617.9, 607.9, 604.3, 619.9, 610.1, 601.8, 600.9, 602.1, 612.5, 615.8, 608.4, 601.2, 607.6, 619.9, 610.5, 619.4, 617.2, 600.2, 614.4, 613.6, 610.7, 605.3, 612.8, 602.2, 608.6, 609.0, 619.0, 617.5, 605.2, 610.0, 603.5, 618.2, 617.4, 605.9, 612.7, 612.1, 603.0, 615.2, 610.7, 615.5, 610.6, 600.0, 606.4, 600.3, 618.5, 617.5, 616.6, 606.1, 601.1, 617.5, 618.9, 601.7, 609.7, 601.3, 615.2, 615.3, 602.5, 609.5, 610.9, 605.3, 617.4, 608.4, 604.2, 610.7, 614.5, 604.0, 606.2, 619.9, 612.9, 608.7, 610.3, 602.4, 604.4, 606.7, 611.7, 604.6, 604.4, 601.4, 612.6, 604.5, 618.1, 617.1, 601.4, 604.7, 613.3, 604.2, 602.6, 618.7, 611.4, 609.4, 615.6, 616.1, 603.8, 601.9, 608.6, 608.4, 609.3, 614.5, 613.4, 619.6, 601.9, 608.0, 606.7, 617.2, 604.9, 603.8, 608.9, 608.4, 605.5, 604.9, 618.4, 608.8, 617.2, 611.0, 601.0, 619.9, 616.7, 619.3, 618.5, 616.9, 603.3, 609.7, 604.2, 608.0, 601.1, 607.5, 619.7, 605.3, 615.6, 609.1, 608.4, 619.1, 619.9, 611.1, 614.3, 603.0, 605.9, 619.3, 611.5, 610.8, 614.9, 601.1, 611.6, 610.0, 617.0, 603.1, 619.2, 601.6, 603.7, 611.9, 613.5, 604.7, 602.3, 617.8, 604.9, 611.8, 612.3, 608.3, 611.6, 610.4, 618.6, 604.0, 614.3, 604.7, 607.9, 613.4, 605.9, 606.3, 615.0, 601.4, 609.1, 619.9, 619.9, 601.4, 604.2, 605.3, 618.6, 617.6, 617.5, 607.3, 603.1, 616.6, 614.0, 612.2, 619.7, 613.0, 600.1, 616.3, 605.9, 613.2, 618.7, 602.6, 602.3, 602.1, 611.0, 605.4, 612.0, 614.3, 604.0, 612.6, 605.2, 609.7, 618.1, 616.9, 601.8,
                608.4, 605.5, 600.0, 615.4, 612.7, 605.2, 614.8, 611.0, 608.5, 600.1, 601.5, 617.6, 618.0, 610.9, 616.6, 611.6, 602.9, 602.5, 606.1, 617.9, 615.9, 617.2, 617.9, 604.2, 604.9, 602.0, 615.6, 617.6, 608.1, 612.4, 603.0, 618.5, 617.2, 619.5, 616.2, 617.6, 600.4, 614.7, 606.6, 618.6, 616.0, 617.2, 616.2, 605.3, 615.7, 602.1, 617.4, 617.1, 604.4, 616.3, 609.2, 606.1, 615.9, 604.5, 600.4, 603.8, 606.5, 617.2, 619.3, 605.5, 612.8, 607.9, 619.6, 610.7, 618.7, 602.3, 619.4, 603.5, 619.2, 605.3, 602.1, 608.6, 614.5, 606.2, 612.1, 610.2, 607.7, 611.5, 605.0, 614.1, 600.0, 618.5, 610.7, 614.3, 614.8, 613.4, 607.2, 601.3, 613.2, 606.6, 606.2, 616.9, 614.3, 606.0, 606.1, 608.1, 608.0, 605.9, 602.5, 608.4, 618.8, 613.5, 618.0, 612.3, 606.0, 610.9, 600.0, 605.7, 608.5, 611.5, 613.0, 609.2, 608.8, 604.2, 609.4, 618.0, 615.9, 603.3, 601.6, 610.3, 612.6, 606.7, 616.3, 615.0, 613.4, 604.4, 603.9, 600.4, 604.8, 609.5, 616.9, 601.4, 608.2, 612.5, 603.8, 613.9, 609.8, 604.8, 613.1, 600.1, 615.0, 615.4, 602.1, 608.5, 603.5, 619.1, 610.3, 601.0, 604.9, 616.9, 609.1, 616.0, 613.3, 619.7, 611.9, 619.0, 617.8, 612.2, 614.3, 610.0, 616.6, 610.9, 617.9, 614.8, 609.4, 605.1, 604.9, 612.7, 615.3, 610.4, 612.5, 605.4, 601.5, 605.7, 605.4, 606.3, 610.8, 602.7, 604.6, 613.8, 614.1, 601.2, 608.1, 610.8, 608.3, 604.1, 608.4, 618.0, 611.6, 613.9, 617.1, 615.3, 607.6, 600.1, 607.0, 615.0, 617.0, 619.0, 608.3, 614.9, 610.9, 612.0, 604.4, 604.3, 608.7, 600.5, 606.7, 613.5, 608.0, 603.3, 609.3, 602.5, 612.4, 600.5, 607.8, 611.2, 600.5, 612.8, 602.7, 609.2, 601.0, 607.5, 604.2, 606.5, 615.2, 607.5, 615.0, 616.6, 605.0, 601.6, 600.3, 610.7, 619.9, 606.9, 613.0, 615.6, 613.0, 615.0, 618.9, 603.9, 600.4, 603.0, 602.5, 613.3, 611.2, 604.3, 613.9, 615.3, 603.3, 612.1, 614.9, 602.2, 616.3, 619.2, 602.1, 600.5, 606.2, 613.5, 619.1, 607.9, 614.3, 601.5, 613.8, 612.5, 602.0, 615.4, 617.0, 612.0, 602.4, 619.6, 615.6, 606.9, 608.5, 607.4, 610.1, 606.8, 616.9, 616.4, 602.1, 619.2, 612.7, 616.5, 614.1, 608.7, 614.6, 619.3, 605.4, 616.1, 610.7, 609.6, 608.7, 614.6, 605.3, 617.0, 616.6, 601.7, 617.6, 604.8, 609.2, 612.2, 607.5, 600.5, 617.0, 603.6, 604.2, 615.9, 606.8, 617.6, 614.0, 605.5, 600.2, 618.9};

        rand = new Random();

        for (int i = 0; i < this.randomHeader1.length; i++) {
            int randomIndexToSwap = rand.nextInt(this.randomHeader1.length);
            double temp1 = this.randomHeader1[randomIndexToSwap];
            this.randomHeader1[randomIndexToSwap] = this.randomHeader1[i];
            this.randomHeader1[i] = temp1;

            double temp2 = this.randomHeader2[randomIndexToSwap];
            this.randomHeader2[randomIndexToSwap] = this.randomHeader2[i];
            this.randomHeader2[i] = temp2;
        }

        this.doca = new Doca(0.5, 0.7, 50, 10, false);
    }

    @Test
    public void testAddFromNormalDistToDomain() {

        for (int i = 0; i < normalHeader1.length; i++) {
            double[][] X = new double[][]{new double[]{normalHeader1[i]}, new double[]{normalHeader2[i]}};
            doca.addToDomain(X);
        }
    }

    @Test
    public void testAddFromRandomDistToDomain() {
        for (int i = 0; i < randomHeader1.length; i++) {
            double[][] X = new double[][]{new double[]{randomHeader1[i]}, new double[]{randomHeader2[i]}};
            doca.addToDomain(X);
        }
    }

    @Test
    public void testAddFromRandomDistToDeltaDoca() {
        for (int i = 0; i < randomHeader1.length; i++) {
            double[][] X = new double[][]{new double[]{randomHeader1[i]}, new double[]{randomHeader2[i]}};
            doca.addTuple(X);
        }
    }

    @Test
    public void testAddFromNormalDistToDeltaDoca() {
        for (int i = 0; i < randomHeader1.length; i++) {
            double[][] X = new double[][]{new double[]{randomHeader1[i]}, new double[]{randomHeader2[i]}};
            doca.addTuple(X);
        }
    }
}
