package com.test.kafka.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("kafka")
public class KafkaController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 生产者
     * @param name
     * @return
     */
    @GetMapping("/send")
    public String send(@RequestParam("name") String name) {
        //如果在发消息的时候指定了分区，则消息投递到指定的分区
        // 如果没有指定分区，但是消息的key不为空，则基于key的哈希值来选择一个分区
        // 如果既没有指定分区，且消息的key也是空，则用轮询的方式选择一个分区
        kafkaTemplate.send("mytopic", name);
        return name;
    }

    /**
     * 消费者
     * @param record
     */
    @KafkaListener(topics = "mytopic")
    public void listen(ConsumerRecord<?,String> record) {
        String value = record.value();
        System.out.println("kafkaTest:"+value);
        System.out.println("kafkaTest:"+record);
    }

    @GetMapping("/send1")
    public String sendMsg(@RequestParam("name") String name) {

        kafkaTemplate.send("mytopic", 1,"ttt",name);
        return name;
    }

    /**
     * 消费者
     * @param record
     */
    @KafkaListener(topics = "mytopic",groupId = "group1")
    public void listenGroupId(ConsumerRecord<?,String> record) {
        String value = record.value();
        System.out.println("kafkaTest:"+value);
        System.out.println("kafkaTest:"+record);
    }
}
