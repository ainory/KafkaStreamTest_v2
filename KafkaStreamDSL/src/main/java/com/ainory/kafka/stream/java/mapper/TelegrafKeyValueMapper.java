package com.ainory.kafka.stream.java.mapper;

import com.ainory.kafka.stream.java.model.TelegrafInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;

public class TelegrafKeyValueMapper implements KeyValueMapper<String, String, KeyValue<String, TelegrafInfo>> {

  @Override
  public KeyValue<String, TelegrafInfo> apply(String key, String value) {

    try{
      ObjectMapper objectMapper = new ObjectMapper();

      TelegrafInfo telegrafInfo = objectMapper.readValue(value, TelegrafInfo.class);

      return new KeyValue<>(String.valueOf(telegrafInfo.getTimestamp()), telegrafInfo);
    }catch (Exception e){
      e.printStackTrace();

      return null;
    }
  }
}
