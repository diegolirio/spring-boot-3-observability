package com.devspoint.springboot3micrometertracingaddresses.controller;

import com.devspoint.springboot3micrometertracingaddresses.entity.AddressEntity;
import com.devspoint.springboot3micrometertracingaddresses.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private static final Logger log = LoggerFactory.getLogger(AddressController.class);
    private final AddressRepository addressRepository;

    public AddressController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @GetMapping("/customers/{customerId}")
    public List<AddressEntity> getAddressesByCustomerId(@PathVariable("customerId") Long customerId) {
        log.info("method=getAddressesByCustomerId, step=starting, customerId={}", customerId);
        return addressRepository.findByCustomerId(customerId);
    }
}
