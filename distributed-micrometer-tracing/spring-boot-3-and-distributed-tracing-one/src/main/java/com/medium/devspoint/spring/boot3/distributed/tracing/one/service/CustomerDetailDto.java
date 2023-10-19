package com.medium.devspoint.spring.boot3.distributed.tracing.one.service;

import com.medium.devspoint.spring.boot3.distributed.tracing.one.entity.Detail;

public record CustomerDetailDto(Long customerId, Detail detail) {
}
