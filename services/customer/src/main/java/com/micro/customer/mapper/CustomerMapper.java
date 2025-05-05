package com.micro.customer.mapper;

import org.springframework.stereotype.Component;

import com.micro.customer.dto.request.CustomerRequest;
import com.micro.customer.dto.response.CustomerResponse;
import com.micro.customer.entity.Customer;

@Component
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest request) {
        System.out.println(45334);
        if (request == null) {
            return null;
        }
        return Customer.builder()
                .id(request.id())
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .address(request.address())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}
