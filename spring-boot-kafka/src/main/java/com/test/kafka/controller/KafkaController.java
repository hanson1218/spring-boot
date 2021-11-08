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
}
