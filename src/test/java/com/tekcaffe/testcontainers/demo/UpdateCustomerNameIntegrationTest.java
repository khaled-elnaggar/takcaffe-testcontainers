package com.tekcaffe.testcontainers.demo;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class UpdateCustomerNameIntegrationTest {

  @Container
  static final KafkaContainer kafka = new KafkaContainer(
          DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
  );


  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }




  @Autowired
  private CustomerService customerService;

  @BeforeEach
  void setUp() {
    customerService.deleteCustomers();
    customerService.registerCustomers(List.of(new Customer("Ahmed")));
  }

  @Test
  void whenCustomerNameChanges_SystemShouldHandleItSuccessfully() {
    // given
    final String newName = "Mohammed";
    Long customerId = 1L;

    // when
    customerService.updateCustomerName(customerId, newName);

    // then
    await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(30, SECONDS)
            .untilAsserted(() -> {
              List<Customer> customers = customerService.getCustomers();

              Assertions.assertThat(customers).isNotNull().isNotEmpty().hasSize(1);
              Customer customer = customers.get(0);

              assertThat(customer.getName()).isEqualTo(newName);
            });
  }
}
