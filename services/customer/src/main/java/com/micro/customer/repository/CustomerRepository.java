package com.micro.customer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.micro.customer.entity.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {

}
