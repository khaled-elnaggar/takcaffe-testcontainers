package com.tekcaffe.testcontainers.demo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class CustomerGreetingTest {
  @Autowired
  private CustomerService customerService;

  public static final int CONTAINER_PORT = 9876;

  public static final int HOST_PORT = 9876;

  @Container
  static final FixedHostPortGenericContainer<?> mySpecialServiceContainer =
          new FixedHostPortGenericContainer<>("special/my-external-service")
                  .withFixedExposedPort(HOST_PORT, CONTAINER_PORT)
                  .waitingFor(Wait.forHttp("/"));

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("external-services.my-special-service.port", () -> CONTAINER_PORT);
  }

  @Test
  void testGenericContainer() {
    String greeting = customerService.getGreeting();
    assertThat(greeting).containsIgnoringCase("Hello World");
    System.out.println("Connection successful and response = " + greeting);
  }

}
