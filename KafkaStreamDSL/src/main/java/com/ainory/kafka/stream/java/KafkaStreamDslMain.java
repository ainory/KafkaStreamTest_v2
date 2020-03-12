package com.ainory.kafka.stream.java;

import com.ainory.kafka.stream.java.dsl.KafkaStreamDsl;

public class KafkaStreamDslMain {

  public static void main(String[] args) {

    KafkaStreamDsl kafkaStreamDsl = new KafkaStreamDsl();

    kafkaStreamDsl.streamDsl();

  }

}
