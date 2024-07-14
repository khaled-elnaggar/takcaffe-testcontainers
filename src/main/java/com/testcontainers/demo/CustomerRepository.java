package com.testcontainers.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Modifying
  @Query("update Customer c set c.name = :newName where c.id = :customerId")
  void updateCustomerName(
          @Param("customerId") Long customerId,
          @Param("newName") String newName
  );
}
