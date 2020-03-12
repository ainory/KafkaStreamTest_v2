package com.ainory.kafka.stream.serializer;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

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
