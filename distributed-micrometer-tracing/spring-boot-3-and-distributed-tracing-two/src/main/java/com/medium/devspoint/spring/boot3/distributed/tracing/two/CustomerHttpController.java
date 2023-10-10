package com.medium.devspoint.spring.boot3.distributed.tracing.two;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/v1/customers")
public class CustomerHttpController {

    private Logger log = LoggerFactory.getLogger(CustomerHttpController.class);

    @GetMapping("/{id}/details")
    public CustomerDetail getDetails(@PathVariable("id") Long id) {
        log.info("m=getDetails, step=init, id={}", id);
        var cd = new CustomerDetail(
                new Random().nextLong(100000000, 999999999),
                LocalDate.now()
        );
        log.info("m=getDetails, step=end, id={}, customerDetail={}", id, cd);
        return cd;
    }
}
