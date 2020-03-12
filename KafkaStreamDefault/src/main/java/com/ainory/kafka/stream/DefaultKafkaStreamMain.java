package com.ainory.kafka.stream;


import com.ainory.kafka.stream.model.CountInfo;
import com.ainory.kafka.stream.processor.TelegrafProcessorSupplier;
import com.ainory.kafka.stream.serializer.CountDeserializer;
import com.ainory.kafka.stream.serializer.CountSerializer;
import com.ainory.kafka.stream.serializer.TelegrafDeserializer;
import com.ainory.kafka.stream.serializer.TelegrafSerializer;
import com.ainory.kafka.stream.timestamp.extractor.TelegrafTimestampExtractor;
import java.util.Properties;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultKafkaStreamMain {

  private static final Logger logger = LoggerFactory.getLogger(DefaultKafkaStreamMain.class);

  public static void main(String[] args) {

    try{


      TelegrafSerializer telegrafSerializer = new TelegrafSerializer();
      TelegrafDeserializer telegrafDeserializer = new TelegrafDeserializer();

      CountDeserializer countDeserializer = new CountDeserializer();
      CountSerializer countSerializer = new CountSerializer();

      Topology topology = new Topology();

      topology.addSource("INPUT", new StringDeserializer(), new StringDeserializer(), "numeric");
      topology.addProcessor("PROCESSOR", new TelegrafProcessorSupplier(), "INPUT");

      StoreBuilder<KeyValueStore<String, CountInfo>> hostMetricStoreSupplier =
          Stores.keyValueStoreBuilder(
              Stores.inMemoryKeyValueStore("Count"),
              Serdes.String(), Serdes.serdeFrom(countSerializer, countDeserializer));
      topology.addStateStore(hostMetricStoreSupplier, "PROCESSOR");

      topology.addSink("OUTPUT", "stream_result", "PROCESSOR");

      Properties props = new Properties();
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "telegraf-stream-1");
      props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "oidev01:9092,oidev02:9092,oidev03:9092,oidev04:9092,oidev05:9092");
      props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
      props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
      props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
      props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, TelegrafTimestampExtractor.class);

      props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);

      StreamsConfig config = new StreamsConfig(props);

      KafkaStreams streams = new KafkaStreams(topology, config);
      streams.start();


    }catch (Exception e){
      logger.error(ExceptionUtils.getStackTrace(e));
    }
  }

}
