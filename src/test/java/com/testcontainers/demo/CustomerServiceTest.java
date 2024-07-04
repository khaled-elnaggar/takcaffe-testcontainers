package com.testcontainers.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerServiceTest {


//  ===========================================

  @Autowired
  CustomerService customerService;

  @Test
  void shouldGetAllCustomers() {
    // Given
    List<Customer> customers = List.of(
            new Customer("John"),
            new Customer("Dennis")
    );

    // When
    customerService.registerCustomers(customers);

    // Then
    List<Customer> savedCustomers = customerService.getCustomers();

    assertThat(savedCustomers)
            .isNotNull()
            .hasSize(customers.size())
            .allMatch(customer -> new Date().after(customer.getRegistrationDate()));

  }
}
