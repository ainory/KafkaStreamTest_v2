package com.ainory.kafka.stream.java.model;

import java.io.Serializable;
import java.util.HashMap;

public class TelegrafInfo implements Serializable {

  /*
  {
    "fields": {
      "usage_active": 0.40000006556510925,
      "usage_guest": 0,
      "usage_guest_nice": 0,
      "usage_idle": 99.59999993443489,
      "usage_iowait": 0,
      "usage_irq": 0,
      "usage_nice": 0,
      "usage_perc": 0.40000006556510925,
      "usage_softirq": 0,
      "usage_steal": 0,
      "usage_system": 0.20000000004074536,
      "usage_user": 0.20000000018626451
    },
    "name": "cpu",
    "tags": {
      "cpu": "cpu42",
      "host": "tcore-oi-data-5",
      "send_time": "1583719401004",
      "tcore_id": "1041190"
    },
    "timestamp": 1583719400000
  }
   */

  private HashMap fields;
  private String name;
  private HashMap tags;
  private long timestamp;

  public HashMap getFields() {
    return fields;
  }

  public void setFields(HashMap fields) {
    this.fields = fields;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public HashMap getTags() {
    return tags;
  }

  public void setTags(HashMap tags) {
    this.tags = tags;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "TelegrafInfo{" +
        "fields=" + fields +
        ", name='" + name + '\'' +
        ", tags=" + tags +
        ", timestamp=" + timestamp +
        '}';
  }
}
