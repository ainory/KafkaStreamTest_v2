package com.ainory.kafka.stream.processor;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

public class TelegrafProcessorSupplier implements ProcessorSupplier {

  @Override
  public Processor get() {
    return new TelegrafProcessor();
  }
}
