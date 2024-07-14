package com.tekcaffe.testcontainers.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class CustomerServiceTest {

  @Autowired
  CustomerService customerService;

  @BeforeEach
  void setUp() {
    customerService.deleteCustomers();
  }

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
