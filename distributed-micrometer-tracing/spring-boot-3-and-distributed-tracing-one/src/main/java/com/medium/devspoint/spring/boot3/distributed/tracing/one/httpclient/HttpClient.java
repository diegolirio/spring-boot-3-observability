package com.medium.devspoint.spring.boot3.distributed.tracing.one.httpclient;

import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Customer;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Detail;
import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.HttpType;

public interface HttpClient {

    HttpType getType();
    Detail getCustomerDetail(Customer customer);

    Detail saveDetail(Detail detail);
}

