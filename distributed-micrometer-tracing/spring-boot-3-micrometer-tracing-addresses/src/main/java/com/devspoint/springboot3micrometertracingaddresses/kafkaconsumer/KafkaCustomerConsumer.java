package com.devspoint.springboot3micrometertracingaddresses.kafkaconsumer;

import com.devspoint.springboot3micrometertracingaddresses.entity.AddressEntity;
import com.devspoint.springboot3micrometertracingaddresses.repository.AddressRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KafkaCustomerConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaCustomerConsumer.class);
    private final AddressRepository addressRepository;

    public KafkaCustomerConsumer(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @KafkaListener(topics = "topic.customer.updated", groupId = "addresses")
    public void listenGroupAddresses(String message) throws JsonProcessingException {
        log.info("m=listenGroupAddresses, step=init, message={}", message);
        CustomerDto dto = new ObjectMapper().readValue(message, CustomerDto.class);
        if (dto.getAddresses() == null || dto.getAddresses().isEmpty()) {
            log.warn("m=listenGroupAddresses, step=not_contains_address");
            return;
        }
        List<AddressEntity> address = dto.getAddresses().stream().peek(it -> it.setCustomerId(dto.getId())).collect(Collectors.toList());
        addressRepository.saveAll(address);
        log.info("m=listenGroupAddresses, step=finished");
    }
}
