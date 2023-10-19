package com.medium.devspoint.spring.boot3.distributed.tracing.two;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/customers")
public class CustomerHttpController {

    private static final Logger log = LoggerFactory.getLogger(CustomerHttpController.class);
    private final CustomerDetailService customerDetailService;

    public CustomerHttpController(CustomerDetailService customerDetailService) {
        this.customerDetailService = customerDetailService;
    }

    @GetMapping("/{id}/details")
    public CustomerDetail getDetails(@PathVariable("id") Long id) {
        log.info("m=getDetails, step=init, id={}", id);
        CustomerDetail cd = customerDetailService.getById(id);
        log.info("m=getDetails, step=end, id={}, customerDetail={}", id, cd);
        return cd;
    }
}
