package com.ainory.kafka.stream.java.serializer;

import java.util.Map;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

public class CountDeserializer<CountInfo> implements Deserializer<CountInfo> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {

  }

  @Override
  public CountInfo deserialize(String topic, byte[] data) {
    if(data == null){
      return null;
    }

    return (CountInfo) SerializationUtils.deserialize(data);
  }

  @Override
  public void close() {

  }
}
