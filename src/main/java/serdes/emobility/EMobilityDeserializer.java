package serdes.emobility;

import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationException;
public class EMobilityDeserializer<T> implements Deserializer<T> {
  private ObjectMapper om = new ObjectMapper();
  private Class<T> type;

  public EMobilityDeserializer(Class<T> type){
    this.type = type;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    if (type == null) {
      type = (Class<T>) configs.get("type");
    }
  }

  @Override
  public T deserialize(String s, byte[] bytes) {
    T data = null;
    if (bytes == null || bytes.length == 0) {
      return null;
    }

    try {
      data = om.readValue(bytes, type);
    } catch (Exception e) {
      throw new SerializationException(e);
    }

    return data;
  }
}