package com.ainory.kafka.stream.processor;

import com.ainory.kafka.stream.model.CountInfo;
import com.ainory.kafka.stream.model.TelegrafInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.Punctuator;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelegrafProcessor extends AbstractProcessor<String, String> {

  private static final Logger logger = LoggerFactory.getLogger(TelegrafProcessor.class);

  private ProcessorContext processorContext;
  private KeyValueStore<String, CountInfo> store;

  private final long CHECK_INTERVAL_SEC = 60;

  private final int SUMMARY_INTERVAL_SEC = 60;

  private long summaryCheckStartTime = 0;



  @Override
  public void init(ProcessorContext context) {
    this.processorContext = context;
    this.store = (KeyValueStore) this.processorContext.getStateStore("Count");

    // registered schedule
    this.processorContext.schedule(CHECK_INTERVAL_SEC*1000, PunctuationType.WALL_CLOCK_TIME, new Punctuator() {
      @Override
      public void punctuate(long l) {

        if(summaryCheckStartTime == 0){
          summaryCheckStartTime = getStartTime(l, SUMMARY_INTERVAL_SEC);
          System.out.println("First summaryCheckTime Setting : "+ DateFormatUtils
              .format(summaryCheckStartTime, "yyyy-MM-dd HH:mm:ss"));
          return;
        }

        long checkTime = summaryCheckStartTime + (SUMMARY_INTERVAL_SEC*1000);

        if(checkTime > l){
//                    System.out.println(DateFormatUtils.format((summaryCheckTime + (SUMMARY_INTERVAL_SEC*1000)), "yyyy-MM-dd HH:mm:ss") + " : " + DateFormatUtils.format(l, "yyyy-MM-dd HH:mm:ss"));
          return;
        }
//                System.out.println("!!! " + DateFormatUtils.format((summaryCheckStartTime + (SUMMARY_INTERVAL_SEC*1000)), "yyyy-MM-dd HH:mm:ss") + " : " + DateFormatUtils.format(l, "yyyy-MM-dd HH:mm:ss"));

        KeyValueIterator<String, CountInfo> iter = store.all();
        ArrayList<String> keyList = new ArrayList<>();
        String key = "";
        while(iter.hasNext()){
          key = iter.next().key;
          keyList.add(key);

          CountInfo countInfo = store.get(key);

          if(countInfo == null){
            continue;
          }

          // send next topic
          processorContext.forward(DateFormatUtils.format(l, "yyyy-MM-dd HH:mm:ss"), countInfo.toString());
        }

        // delete store data
        for(String host_key : keyList){
          store.delete(host_key);
        }

        System.out.println(DateFormatUtils.format(summaryCheckStartTime, "yyyy-MM-dd HH:mm:ss") + " punctuate end..");
        summaryCheckStartTime = getStartTime(l, SUMMARY_INTERVAL_SEC);
      }

      public  long getStartTime(long l, int prevSec) {
        int itv = prevSec*1000;
        long ct = l - (prevSec*1000);
        long nct = ((ct/itv)*itv)+itv;
        return nct;
      }
    });
  }

  @Override
  public void process(String key, String value) {

    ObjectMapper objectMapper = new ObjectMapper();

    try{

      TelegrafInfo telegrafInfo = objectMapper.readValue(value, TelegrafInfo.class);

      if(!(StringUtils.equals(telegrafInfo.getName(), "ping") || StringUtils.equals(telegrafInfo.getName(), "net_response"))){
        return;
      }

//      logger.error("!!!!!" + telegrafInfo.toString());

      CountInfo countInfo = this.store.get(DateFormatUtils
          .format(telegrafInfo.getTimestamp(), "yyyy-MM-dd HH:mm:ss"));

      if(countInfo == null){
        countInfo = new CountInfo();
      }

      if(StringUtils.equals(telegrafInfo.getName(), "ping")){
        countInfo.setTimestamp(DateFormatUtils
            .format(telegrafInfo.getTimestamp(), "yyyy-MM-dd HH:mm:ss"));
        countInfo.incPing_cnt();
      }

      if(StringUtils.equals(telegrafInfo.getName(), "net_response")){
        countInfo.setTimestamp(DateFormatUtils
            .format(telegrafInfo.getTimestamp(), "yyyy-MM-dd HH:mm:ss"));
        countInfo.incSsh_cnt();
      }



      this.store.put(DateFormatUtils
          .format(telegrafInfo.getTimestamp(), "yyyy-MM-dd HH:mm:ss"), countInfo);




//      processorContext.forward(String.valueOf(System.currentTimeMillis()), value);

    }catch (Exception e){
      logger.error(ExceptionUtils.getStackTrace(e));
    }

  }
}
