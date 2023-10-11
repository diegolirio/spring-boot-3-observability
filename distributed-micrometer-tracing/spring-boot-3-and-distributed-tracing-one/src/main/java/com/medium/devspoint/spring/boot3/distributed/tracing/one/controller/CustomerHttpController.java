package com.medium.devspoint.spring.boot3.distributed.tracing.one.controller;

import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Customer;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Detail;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.HttpType;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.httpclient.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/customers")
public class CustomerHttpController {

    Logger log = LoggerFactory.getLogger(CustomerHttpController.class);
    private final CustomerService customerService;

    public CustomerHttpController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    //@Observed
    public List<Customer> getAll(@RequestParam(name = "httpType", defaultValue = "REST_TEMPLATE") String httpType) {
        log.info("m=getAll, step=init");
        var list = customerService.getCustomers(HttpType.valueOf(httpType));
        log.info("m=getAll, step=end, list={}", list);
        return list;
    }

    @PostMapping
    public Customer post(@RequestBody Customer customer) {
        log.info("m=post, step=init, customer={}", customer);
        var c = customerService.save(customer);
        log.info("m=post, step=init, customerId={}", c.id());
    }

}

@Component
class CustomerService {

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
        log.info("m=save, customer={}", customer);
        var detail = httpClientList.get(0).saveDetail(customer.detail());
        return new Customer(new Random().nextLong(), customer.name(), detail);
    }

}
