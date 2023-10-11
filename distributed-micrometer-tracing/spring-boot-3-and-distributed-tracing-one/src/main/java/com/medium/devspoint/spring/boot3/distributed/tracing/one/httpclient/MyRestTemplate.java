package com.medium.devspoint.spring.boot3.distributed.tracing.one.httpclient;

import brave.Tracer;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Customer;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Detail;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.HttpType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class MyRestTemplate  implements HttpClient {

    private final String customerDetailHost;
    Logger log = LoggerFactory.getLogger(MyRestTemplate.class);
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

    @Override
    public HttpType getType() {
        return HttpType.REST_TEMPLATE;
    }

    @Override
    public Detail getCustomerDetail(Customer customer) {
        var response = restTemplate
                .getForEntity(
                        customerDetailHost + "/v1/customers/" + customer.id() + "/details",
                        Detail.class
                );
        log.info("m=getCustomerDetail, trace={}, response={}", tracer.currentSpan().context().traceId(), response);
        return response.getBody();
    }

    @Override
    public Detail saveDetail(Detail detail) {
        return null;
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


