package com.medium.devspoint.spring.boot3.distributed.tracing.one.controller;

import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Customer;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.HttpType;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerHttpController {

    private final Logger log = LoggerFactory.getLogger(CustomerHttpController.class);
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
    public void post(@RequestBody Customer customer) {
        log.info("m=post, step=init, customer={}", customer);
        var c = customerService.save(customer);
        log.info("m=post, step=end, customerId={}", c.id());
    }

}

