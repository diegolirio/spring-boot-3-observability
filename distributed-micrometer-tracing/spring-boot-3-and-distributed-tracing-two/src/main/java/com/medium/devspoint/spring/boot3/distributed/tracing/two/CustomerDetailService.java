package com.medium.devspoint.spring.boot3.distributed.tracing.two;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class CustomerDetailService {

    private static final Logger log = LoggerFactory.getLogger(CustomerDetailService.class);

    private static Map<Long, CustomerDetail> detailMap = new HashMap<>();

    public CustomerDetail getById(Long customerId) {
        log.info("m=getById, step=init, customerId={}", customerId);
        var cd = detailMap.get(customerId);
        //        var cd = new CustomerDetail(
        //                new Random().nextLong(100000000, 999999999),
        //                LocalDate.now()
        //        );
        log.info("m=getById, step=end, customerId={}, customerDetail={}", customerId, cd);
        return cd;
    }

    public CustomerDetail saveDetail(Long customerId, CustomerDetail customerDetail) {
        log.info("m=saveDetail, step=init, customerId={}, customerDetail={}", customerId, customerDetail);
        if(detailMap.get(customerId) != null) {
            log.warn("m=saveDetail, step=already_exists, customerId={}, customerDetail={}", customerId, customerDetail);
            return customerDetail;
        }
        log.info("m=saveDetail, step=end, customerId={}, customerDetail={}", customerId, customerDetail);
        return detailMap.put(customerId, customerDetail);
    }
}
