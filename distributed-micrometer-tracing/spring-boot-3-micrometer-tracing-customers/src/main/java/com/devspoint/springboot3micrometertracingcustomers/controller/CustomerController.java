package com.devspoint.springboot3micrometertracingcustomers.controller;

import com.devspoint.springboot3micrometertracingcustomers.entity.CustomerEntity;
import com.devspoint.springboot3micrometertracingcustomers.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public CustomerEntity getById(@PathVariable("id") Long id) {
        log.info("method=getById, step=starting, id={}", id);
        var customer = customerService.getById(id);
        log.info("method=getById, step=finished, id={}, customer={}", id, customer);
        return customer;
    }

    @PostMapping
    public CustomerEntity create(@RequestBody CustomerEntity customerEntity) {
        log.info("method=create, step=starting, customerEntity={}", customerEntity);
        // TODO Add o location
        var customer = customerService.create(customerEntity);
        log.info("method=create, step=finished, customerId={}", customerEntity.getId());
        return customer;
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> notFoundHandleException(Exception e) {
        var map = new HashMap<String, String>();
        map.put("message", "Customer Not Found");
        log.warn("method=notFoundHandleException, step=not_found, e={}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(map);
    }
}
