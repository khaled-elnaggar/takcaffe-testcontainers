package com.testcontainers.demo;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class CustomerGreetingTest {
  @Autowired
  private TestRestTemplate restTemplate;

  private static final String serviceUri = "http:// localhost";
  private static final int servicePort = 9876;
  private static int mappedPort = -1;

  private static final DockerImageName IMAGE_NAME = DockerImageName.parse("special/my-external-service");

  @Container
  static final GenericContainer<?> mySpecialServiceContainer = new GenericContainer<>(IMAGE_NAME)
          .withExposedPorts(servicePort);

  @BeforeAll
  static void beforeAll() {
    mappedPort = mySpecialServiceContainer.getMappedPort(servicePort);
  }


  @Test
  void testGenericContainer() {
    String url = serviceUri + ":" + mappedPort;

    ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
    String response = responseEntity.getBody();

    assertThat(response).containsIgnoringCase("Hello World");
    System.out.println("Connection successful and response = " + response);
  }
}
