package com.medium.devspoint.spring.boot3.distributed.tracing.one;

import brave.Tracer;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerHttpController {

    Logger log = LoggerFactory.getLogger(CustomerHttpController.class);
    private final ServiceTwo serviceTwo;

    public CustomerHttpController(ServiceTwo serviceTwo) {
        this.serviceTwo = serviceTwo;
    }

    @GetMapping
    @Observed
    public List<Customer> getAll() {
        log.info("m=getAll, step=init");
        var list = serviceTwo.getCustomers();
        log.info("m=getAll, step=end, list={}", list);
        return list;
    }


}

@Component
class ServiceTwo {

    private final MyRestTemplate restTemplate;
    Logger log = LoggerFactory.getLogger(ServiceTwo.class);

    ServiceTwo(MyRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Customer> getCustomers() {
        var customers = List.of(new Customer(1L, "Diego", null));
        return customers.stream().map(it -> {
            Detail detail = restTemplate.getCustomerDetail(it);
            return new Customer(it.id(), it.name(), detail);
        }).toList();
    }

}

@Component
class MyRestTemplate {

    private final String customerDetailHost;
    Logger log = LoggerFactory.getLogger(ServiceTwo.class);
    final RestTemplate restTemplate;
    final Tracer tracer;

    MyRestTemplate(
            RestTemplate restTemplate,
            Tracer tracer,
            @Value("${services.apis.customer-details}") String customerDetailHost
    ) {
        this.restTemplate = restTemplate;
        this.tracer = tracer;
        this.customerDetailHost = customerDetailHost;
    }

    public Detail getCustomerDetail(Customer customer) {
        var response = restTemplate
                .getForEntity(
                    customerDetailHost + "/v1/customers/"+customer.id()+"/details",
                    Detail.class
                );
        log.info("m=getCustomerDetail, trace={}, response={}", tracer.currentSpan().context().traceId(), response);
        return response.getBody();
    }


}

@Configuration(proxyBeanMethods = false)
class MyConfiguration {
    // IMPORTANT! To instrument RestTemplate you must inject the RestTemplateBuilder
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
