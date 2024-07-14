package com.tekcaffe.testcontainers.demo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@DirtiesContext
class CustomerGreetingTest {
  @Autowired
  private CustomerService customerService;

  static private final int containerPort = MySpecialServiceConfigurations.servicePort;

  static private final int hostPort = MySpecialServiceConfigurations.servicePort;

  @Container
  static final FixedHostPortGenericContainer<?> mySpecialServiceContainer =
          new FixedHostPortGenericContainer<>("special/my-external-service")
          .withFixedExposedPort(hostPort, containerPort);

  @Test
  void testGenericContainer() {
    String greeting = customerService.getGreeting();
    assertThat(greeting).containsIgnoringCase("Hello World");
    System.out.println("Connection successful and response = " + greeting);
  }

}
