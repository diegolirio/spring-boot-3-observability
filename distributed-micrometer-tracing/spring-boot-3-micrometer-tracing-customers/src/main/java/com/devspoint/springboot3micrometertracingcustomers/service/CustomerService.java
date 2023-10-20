package com.devspoint.springboot3micrometertracingcustomers.service;

import com.devspoint.springboot3micrometertracingcustomers.entity.AddressEntity;
import com.devspoint.springboot3micrometertracingcustomers.entity.CustomerEntity;
import com.devspoint.springboot3micrometertracingcustomers.httpclient.AddressHttpClient;
import com.devspoint.springboot3micrometertracingcustomers.kafkaproducer.CustomerUpdateKafkaProducer;
import com.devspoint.springboot3micrometertracingcustomers.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final AddressHttpClient addressHttpClient;
    private final CustomerUpdateKafkaProducer producer;

    public CustomerService(CustomerRepository customerRepository, AddressHttpClient addressHttpClient, CustomerUpdateKafkaProducer producer) {
        this.customerRepository = customerRepository;
        this.addressHttpClient = addressHttpClient;
        this.producer = producer;
    }

    public CustomerEntity getById(Long id) {
        log.info("method=getById, id={}", id);
        CustomerEntity customerEntity = customerRepository.findById(id).get();
        List<AddressEntity> addresses = addressHttpClient.getAddressesByCustomerId(customerEntity.getId());
        log.info("method=getById, id={}, addresses={}", id, addresses);
        customerEntity.setAddresses(addresses);
        return customerEntity;
    }

    public CustomerEntity create(CustomerEntity customerEntity) {
        log.info("method=create, customerEntity={}", customerEntity);
        CustomerEntity saved = customerRepository.save(customerEntity);
        producer.send(saved);
        return saved;
    }
}
