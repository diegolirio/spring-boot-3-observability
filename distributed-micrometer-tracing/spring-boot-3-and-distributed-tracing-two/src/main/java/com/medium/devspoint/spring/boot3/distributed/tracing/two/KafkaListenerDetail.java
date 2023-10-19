package com.medium.devspoint.spring.boot3.distributed.tracing.two;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerDetail {

    private final Logger log = LoggerFactory.getLogger(KafkaListenerDetail.class);
    private final CustomerDetailService customerDetailService;

    public KafkaListenerDetail(CustomerDetailService customerDetailService) {
        this.customerDetailService = customerDetailService;
    }

    @KafkaListener(topics = "topic.customer.detail", groupId = "two")
    public void listenGroupTwo(String message) throws JsonProcessingException {
        log.info("m=listenGroupTwo, step=init, message={}", message);
        CreateCustomerDetailDtoAvro dto = new ObjectMapper().readValue(message, CreateCustomerDetailDtoAvro.class);
        customerDetailService.saveDetail(dto.customerId(), dto.detail());

        log.info("m=listenGroupTwo, message={}", message);
    }

}
