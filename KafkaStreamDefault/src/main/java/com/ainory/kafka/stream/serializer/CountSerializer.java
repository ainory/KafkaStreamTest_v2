package com.ainory.kafka.stream.serializer;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

import java.io.*;
import java.util.Map;

public class CountSerializer<CountInfo> implements Serializer<CountInfo> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {

  }

  @Override
  public byte[] serialize(String topic, CountInfo data) {
    return SerializationUtils.serialize((Serializable) data);
  }

  @Override
  public void close() {

  }
}
