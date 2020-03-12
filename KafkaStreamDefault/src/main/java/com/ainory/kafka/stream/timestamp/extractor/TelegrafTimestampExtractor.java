package com.ainory.kafka.stream.timestamp.extractor;

import com.ainory.kafka.stream.model.TelegrafInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

/**
 * @author ainory on 2018. 3. 29..
 */
public class TelegrafTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long l) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            TelegrafInfo telegrafInfo = objectMapper.readValue(consumerRecord.value().toString(), TelegrafInfo.class);

            try {
                return telegrafInfo.getTimestamp();
            } catch (Exception e) {
                e.printStackTrace();
                return System.currentTimeMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }
}
