package com.test.kafka.constants;

public enum  DelayStrategy {

    S10("S",10,"延迟10S"),

    M("M1",1,"延迟1分钟"),
    H1("H10",10,"延迟10小时");

    private String delayLevel;
    private int time;
    private String desc;

    DelayStrategy(String delayLevel, int time, String desc) {
        this.delayLevel = delayLevel;
        this.time = time;
        this.desc = desc;
    }
}
