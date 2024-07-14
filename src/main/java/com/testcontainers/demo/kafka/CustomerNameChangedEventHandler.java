package com.testcontainers.demo.kafka;

import com.testcontainers.demo.CustomerRepository;
import com.testcontainers.demo.kafka.payload.CustomerNameChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
class CustomerNameChangedEventHandler {

  private static final Logger log = LoggerFactory.getLogger(
    CustomerNameChangedEventHandler.class
  );

  private final CustomerRepository customerRepository;

  CustomerNameChangedEventHandler(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @KafkaListener(topics = "customer-name-changes", groupId = "demo")
  public void handle(CustomerNameChangedEvent event) {
    log.info(
      "Received a new name for customerId:{}: ",
      event.customerId()
    );

    customerRepository.updateCustomerName(event.customerId(), event.newName());
  }
}
