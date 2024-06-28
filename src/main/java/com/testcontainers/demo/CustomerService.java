package com.testcontainers.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerService {
  @Autowired
  CustomerRepository customerRepository;

  public void registerCustomers(List<Customer> customers) {
    for (Customer customer : customers) {
      customer.setRegistrationDate(new Date());
    }
    customerRepository.saveAll(customers);
  }

  public List<Customer> getCustomers() {
    return customerRepository.findAll();
  }
}
