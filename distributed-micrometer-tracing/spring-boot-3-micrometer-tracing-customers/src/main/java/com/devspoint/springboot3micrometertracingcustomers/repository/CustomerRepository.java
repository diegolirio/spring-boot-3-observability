package com.devspoint.springboot3micrometertracingcustomers.repository;

import com.devspoint.springboot3micrometertracingcustomers.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

}
