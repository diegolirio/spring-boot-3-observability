package com.devspoint.springboot3micrometertracingaddresses.kafkaconsumer;

import com.devspoint.springboot3micrometertracingaddresses.entity.AddressEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Transient;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDto {
    private Long id;
    private List<AddressEntity> addresses;

    public Long getId() { return id; }
    public List<AddressEntity> getAddresses() {return addresses;}
    public void setId(Long id) { this.id = id; }
    public void setAddresses(List<AddressEntity> addresses) {this.addresses = addresses;}

}
