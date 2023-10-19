package com.medium.devspoint.spring.boot3.distributed.tracing.one.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Customer;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Detail;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.HttpType;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.httpclient.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class CustomerService {

    @Autowired
    private KafkaTemplate<String, String> template;

    private final List<HttpClient> httpClientList;
    Logger log = LoggerFactory.getLogger(CustomerService.class);

    CustomerService(List<HttpClient> httpClientList) {
        this.httpClientList = httpClientList;
    }

    public List<Customer> getCustomers(HttpType httpType) {

        var customers = List.of(new Customer(1L, "Diego", null));

        HttpClient httpClient = httpClientList
                .stream()
                .filter(it -> it.getType() == httpType)
                .toList()
                .get(0);

        return customers.stream().map(it -> {
            Detail detail = httpClient.getCustomerDetail(it);
            return new Customer(it.id(), it.name(), detail);
        }).toList();
    }

    public Customer save(Customer customer) {
        log.info("m=save, step=init, customer={}", customer);
        var customerSaved = new Customer(new Random().nextLong(), customer.name(), null);
        if (customer.detail() != null) {
            try {
                var cdd = new CustomerDetailDto(customerSaved.id(), new Detail(customer.detail().document(), null));
                var jsonStr = new ObjectMapper().writeValueAsString(cdd);
                this.sendMessage(jsonStr); // producer
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            log.info("m=save, step=send-detail, msg=Enviado para o topico");
        }
        log.info("m=save, step=end");
        return customerSaved;
    }

    public void sendMessage(String message) {
        var future = template.send("topic.customer.detail", message); //.isDone();
        log.info("m=sendMessage, msg=It is not a future, future={}", future);
//        future.whenComplete((result, ex) -> {
//            if (ex == null) {
//                log.info("m=sendMessage, msg=Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
//            } else {
//                log.error("m=sendMessage, msg=Unable to send message=[" + message + "] due to :" + ex.getMessage());
//            }
//        });
    }


}
