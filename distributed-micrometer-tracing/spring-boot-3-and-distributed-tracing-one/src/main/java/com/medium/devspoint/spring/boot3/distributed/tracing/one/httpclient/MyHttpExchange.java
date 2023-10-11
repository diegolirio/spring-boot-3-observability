package com.medium.devspoint.spring.boot3.distributed.tracing.one.httpclient;

import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Customer;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Detail;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.HttpType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
class MyHttpExchange implements HttpClient {

    private Logger log = LoggerFactory.getLogger(MyHttpExchange.class);

    private final InnerHttpExchange innerHttpExchange;

    MyHttpExchange(InnerHttpExchange innerHttpExchange) {
        this.innerHttpExchange = innerHttpExchange;
    }

    @Override
    public HttpType getType() {
        return HttpType.HTTP_EXCHANGE;
    }

    @Override
    public Detail getCustomerDetail(Customer customer) {
        log.info("m=getCustomerDetail, step=init");
        var detail = innerHttpExchange.getCustomerDetail(customer.id());
        log.info("m=getCustomerDetail, response={}", detail);
        return detail;
    }

    @Override
    public Detail saveDetail(Detail detail) {
        return innerHttpExchange.save(detail);
    }

    @HttpExchange(
            url = "/v1",
            accept = MediaType.APPLICATION_JSON_VALUE
    )
    interface InnerHttpExchange {

        @GetExchange("/customers/{customerId}/details")
        Detail getCustomerDetail(@PathVariable Long customerId);

        @PostMapping("/customers/{customerId}/details")
        Detail save(@PathVariable Detail detail);

        @Configuration
        class NewHttpExchangeConfig {

            private final WebClient.Builder webClientBuilder;

            @Value("${services.apis.customer-details}")
            String customerDetailHost;

            public NewHttpExchangeConfig(WebClient.Builder webClientBuilder) {
                this.webClientBuilder = webClientBuilder;
            }

            @Bean
            InnerHttpExchange newHttpExchange() {
                var webClient = webClientBuilder //WebClient.builder()
                        .baseUrl(customerDetailHost)
                        .build();
                return HttpServiceProxyFactory
                        .builder(WebClientAdapter.forClient(webClient))
                        .build()
                        .createClient(InnerHttpExchange.class);
            }
        }
    }
}