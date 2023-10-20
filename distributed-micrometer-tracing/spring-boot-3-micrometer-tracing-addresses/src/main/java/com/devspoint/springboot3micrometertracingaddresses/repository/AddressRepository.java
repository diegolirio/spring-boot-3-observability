package com.devspoint.springboot3micrometertracingaddresses.repository;

import com.devspoint.springboot3micrometertracingaddresses.entity.AddressEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findByCustomerId(Long customerId);
}
