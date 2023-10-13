package com.medium.devspoint.spring.boot3.distributed.tracing.two;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerDetail {

    private final Logger log = LoggerFactory.getLogger(KafkaListenerDetail.class);

    @KafkaListener(topics = "topic.customer.detail", groupId = "two")
    public void listenGroupTwo(String message) {
        log.info("m=listenGroupTwo, message={}", message);
    }

}
