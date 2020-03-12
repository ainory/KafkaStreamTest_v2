package com.ainory.kafka.stream.java.serializer;

import com.ainory.kafka.stream.java.model.TelegrafInfo;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class CustomSerde {

  public CustomSerde() {
  }

  public static Serde<TelegrafInfo> telegrafSerdeSerde(){
    return new CustomSerde.TelegrafSerdeVO();
  }

  public static final class TelegrafSerdeVO extends CustomSerde.TelegrafSerde<TelegrafInfo>{
    public TelegrafSerdeVO() {
      super(new TelegrafSerializer<>(), new TelegrafDeserializer<>());
    }
  }

  protected static class TelegrafSerde<TelegrafInfo> implements Serde<TelegrafInfo> {

    private final Serializer<TelegrafInfo> serializer;
    private final Deserializer<TelegrafInfo> deserializer;

    public TelegrafSerde(Serializer<TelegrafInfo> serializer,
        Deserializer<TelegrafInfo> deserializer) {
      this.serializer = serializer;
      this.deserializer = deserializer;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
      serializer.configure(configs, isKey);
      deserializer.configure(configs, isKey);
    }

    @Override
    public void close() {
      serializer.close();
      deserializer.close();
    }

    @Override
    public Serializer<TelegrafInfo> serializer() {
      return serializer;
    }

    @Override
    public Deserializer<TelegrafInfo> deserializer() {
      return deserializer;
    }
  }
}
