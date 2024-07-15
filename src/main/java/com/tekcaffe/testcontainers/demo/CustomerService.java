package com.tekcaffe.testcontainers.demo;

import com.tekcaffe.testcontainers.demo.kafka.payload.CustomerNameChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;


  public void registerCustomers(List<Customer> customers) {
    for (Customer customer : customers) {
      customer.setRegistrationDate(new Date());
    }
    customerRepository.saveAll(customers);
  }

  public List<Customer> getCustomers() {
    return customerRepository.findAll();
  }

  public void deleteCustomers() {
    customerRepository.deleteAll();
  }





















  @Autowired
  private KafkaTemplate<Long, Object> kafkaTemplate;

  public void updateCustomerName(Long customerId, String newName) {
    CustomerNameChangedEvent nameChangedEvent = new CustomerNameChangedEvent(customerId, newName);
    kafkaTemplate.send("customer-name-changes", customerId, nameChangedEvent);
  }
















  @Value("${external-services.my-special-service.uri}")
  private String serviceUri;

  @Value("${external-services.my-special-service.port}")
  private int servicePort;


  public String getGreeting() {
    RestTemplate restTemplate = new RestTemplate();
    String uri = serviceUri + ":" + servicePort;

    ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
    return responseEntity.getBody();
  }
}
