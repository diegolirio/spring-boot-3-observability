package com.devspoint.springboot3micrometertracingcustomers.kafkaproducer;

import com.devspoint.springboot3micrometertracingcustomers.entity.CustomerEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerUpdateKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(CustomerUpdateKafkaProducer.class);
    @Autowired private KafkaTemplate<String, String> kafkaTemplate;

    public void send(CustomerEntity customerEntity) {
        log.info("method=send, step=starting, customerEntity={}", customerEntity);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(customerEntity);
            log.info("method=send, step=send, json={}", json);
            kafkaTemplate.send("topic.customer.updated", json).isDone();
        } catch (JsonProcessingException e) {
            log.info("method=send, step=error, customerEntity={}, e.message={}", customerEntity, e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("method=send, step=finished, customerEntity={}", customerEntity);
    }
}
