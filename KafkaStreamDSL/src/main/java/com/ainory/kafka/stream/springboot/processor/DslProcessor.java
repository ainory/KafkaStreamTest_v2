package com.ainory.kafka.stream.springboot.processor;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public class DslProcessor {

  final String INPUT = "numeric";
  final String OUTPUT = "numeric-result5";

  @Input(INPUT)
  SubscribableChannel subscribableChannel;

  @Output(OUTPUT)
  MessageChannel outboundChannel;


}
