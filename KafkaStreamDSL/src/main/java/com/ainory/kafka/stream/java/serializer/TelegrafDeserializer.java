package com.ainory.kafka.stream.java.serializer;

import java.util.Map;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

public class TelegrafDeserializer<TelegrafInfo> implements Deserializer<TelegrafInfo> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {

  }

  @Override
  public TelegrafInfo deserialize(String topic, byte[] data) {
    if(data == null){
      return null;
    }

    return (TelegrafInfo) SerializationUtils.deserialize(data);
  }

  @Override
  public void close() {

  }
}
