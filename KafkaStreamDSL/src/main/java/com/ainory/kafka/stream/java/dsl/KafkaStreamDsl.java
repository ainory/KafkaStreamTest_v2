package com.ainory.kafka.stream.java.dsl;

import com.ainory.kafka.stream.java.extractor.TelegrafTimestampExtractor;
import com.ainory.kafka.stream.java.mapper.TelegrafKeyValueMapper;
import com.ainory.kafka.stream.java.model.TelegrafInfo;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;


public class KafkaStreamDsl {

  public void streamDsl() {

    try {
      Properties props = new Properties();
      props.put(StreamsConfig.APPLICATION_ID_CONFIG, "telegraf-stream-4");
      props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
          "oidev01:9092,oidev02:9092,oidev03:9092,oidev04:9092,oidev05:9092");
      props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
      props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
      props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
          TelegrafTimestampExtractor.class);
//      props.put(StreamsConfig.STATE_DIR_CONFIG,
//          "/Users/leedongju/Desktop/Dev/Source/Git/KafkaStreamTest_v2/KafkaStreamDSL/state");
      props.put(StreamsConfig.STATE_DIR_CONFIG, Files.createTempDirectory("kafka-stream-test").toAbsolutePath().toString()
          );

      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

      StreamsBuilder builder = new StreamsBuilder();

      // input
      KStream<String, String> stream = builder.stream("numeric");

      // data convert(string json -> object)
      KStream<String, TelegrafInfo> newKeyStream = stream.map(new TelegrafKeyValueMapper());

      // data filtering( check type PING_OOB/SSH_OOB )
      KStream<String, TelegrafInfo>[] branches = newKeyStream.branch(
          (key, value) -> String.valueOf(value.getTags().get("check_type")).equals("PING_OOB"),
          (key, value) -> String.valueOf(value.getTags().get("check_type")).equals("SSH_OOB"));

      // type per loop
      for (KStream<String, TelegrafInfo> stream1 : branches) {

        stream1
            // key/value setting
            .map((KeyValueMapper<String, TelegrafInfo, KeyValue<String, String>>) (key, value)
                    -> new KeyValue<>(String.valueOf(value.getTags().get("check_type")), "1"))
            // check_type grouping
            .groupByKey()
            // period 30 seconds windowing
            .windowedBy(TimeWindows.of(TimeUnit.SECONDS.toMillis(30)))
            // period 30 seconds windowing count
            .count()
            .toStream()
            // output value formatting
            .map(
                (KeyValueMapper<Windowed<String>, Long, KeyValue<String, String>>) (key, value)
                    -> new KeyValue<>(key.key(), "(" + key.key() + ")\t" + DateFormatUtils
                    .format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.sss") + " => "
                    + value))
            // output
            .to("stream-result4");
      }

      final KafkaStreams streams = new KafkaStreams(builder.build(), props);

      // zero seconds start
      while (true) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.SECOND) == 0) {
          break;
        }
        try {
          Thread.sleep(100);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      streams.start();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
