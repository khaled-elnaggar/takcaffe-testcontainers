package com.tekcaffe.testcontainers.demo;

import com.tekcaffe.testcontainers.demo.kafka.payload.CustomerNameChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private KafkaTemplate<Long, Object> kafkaTemplate;

  public String getGreeting() {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://" + MySpecialServiceConfigurations.serviceUri + ":" + MySpecialServiceConfigurations.servicePort;

    ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
    return responseEntity.getBody();
  }

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

  public void updateCustomerName(Long customerId, String newName) {
    CustomerNameChangedEvent nameChangedEvent = new CustomerNameChangedEvent(customerId, newName);
    kafkaTemplate.send("customer-name-changes", customerId, nameChangedEvent);
  }
}
