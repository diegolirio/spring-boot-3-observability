package com.medium.devspoint.spring.boot3.distributed.tracing.one;

import brave.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

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
    //@Observed
    public List<Customer> getAll(@RequestParam(name = "httpType", defaultValue = "REST_TEMPLATE") String httpType) {
        log.info("m=getAll, step=init");
        var list = serviceTwo.getCustomers(HttpType.valueOf(httpType));
        log.info("m=getAll, step=end, list={}", list);
        return list;
    }


}

@Component
class ServiceTwo {

    private final List<HttpClient> httpClientList;
    Logger log = LoggerFactory.getLogger(ServiceTwo.class);

    ServiceTwo(List<HttpClient> httpClientList) {
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

}

interface HttpClient {

    HttpType getType();
    Detail getCustomerDetail(Customer customer);
}

@Component
class MyHttpExchange implements HttpClient {

    private Logger log = LoggerFactory.getLogger(MyHttpExchange.class);

    private final InnerHttpExchange innerHttpExchange;

    MyHttpExchange(InnerHttpExchange innerHttpExchange) {
        this.innerHttpExchange = innerHttpExchange;
    }

    @Override
    public HttpType getType() { return HttpType.HTTP_EXCHANGE; }

    @Override
    public Detail getCustomerDetail(Customer customer) {
        log.info("m=getAll, step=init");
        var detail = innerHttpExchange.getCustomerDetail(customer.id());
        log.info("m=getCustomerDetail, response={}", detail);
        return detail;
    }

    @HttpExchange(
            url = "/v1",
            accept = MediaType.APPLICATION_JSON_VALUE
    )
    interface InnerHttpExchange {

        @GetExchange("/customers/{customerId}/details")
        Detail getCustomerDetail(@PathVariable Long customerId);

        @Configuration
        class NewHttpExchangeConfig {

            private final WebClient.Builder webClientBuilder;

            @Value("${services.apis.customer-details}") String customerDetailHost;

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

@Component
class MyRestTemplate  implements HttpClient {

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

    @Override
    public HttpType getType() { return HttpType.REST_TEMPLATE; }

    @Override
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
