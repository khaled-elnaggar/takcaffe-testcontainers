package com.tekcaffe.testcontainers.demo.kafka.payload;

public record CustomerNameChangedEvent(Long customerId, String newName) {
}