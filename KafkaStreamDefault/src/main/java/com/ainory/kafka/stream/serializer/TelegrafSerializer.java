package com.ainory.kafka.stream.serializer;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

import java.io.*;
import java.util.Map;

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
