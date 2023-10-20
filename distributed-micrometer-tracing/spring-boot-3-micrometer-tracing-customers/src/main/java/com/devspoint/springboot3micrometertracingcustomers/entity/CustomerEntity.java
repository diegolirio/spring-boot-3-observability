package com.devspoint.springboot3micrometertracingcustomers.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id @GeneratedValue private Long id;
    private String name;
    private LocalDate bornAt;
    @Transient private List<AddressEntity> addresses;

    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDate getBornAt() { return bornAt; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBornAt(LocalDate bornAt) { this.bornAt = bornAt; }

    public List<AddressEntity> getAddresses() {return addresses;}

    public void setAddresses(List<AddressEntity> addresses) {this.addresses = addresses;}
}
