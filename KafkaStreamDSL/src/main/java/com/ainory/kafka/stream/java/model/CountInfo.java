package com.ainory.kafka.stream.java.model;

import java.io.Serializable;

public class CountInfo implements Serializable {

  private String timestamp;
  private long ping_cnt = 0;
  private long ssh_cnt = 0;

  public long getPing_cnt() {
    return ping_cnt;
  }

  public void setPing_cnt(long ping_cnt) {
    this.ping_cnt = ping_cnt;
  }

  public void incPing_cnt(){
    this.ping_cnt++;
  }

  public long getSsh_cnt() {
    return ssh_cnt;
  }

  public void setSsh_cnt(long ssh_cnt) {
    this.ssh_cnt = ssh_cnt;
  }

  public void incSsh_cnt() {
    this.ssh_cnt++;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "CountInfo{" +
        "timestamp='" + timestamp + '\'' +
        ", ping_cnt=" + ping_cnt +
        ", ssh_cnt=" + ssh_cnt +
        '}';
  }
}
