package com.ainory.kafka.stream.java.serializer;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

public class TelegrafSerializer<TelegrafInfo> implements Serializer<TelegrafInfo> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {

  }

  @Override
  public byte[] serialize(String topic, TelegrafInfo data) {
    return SerializationUtils.serialize((Serializable) data);
  }

  @Override
  public void close() {

  }
}
