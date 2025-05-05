package com.micro.customer.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.micro.customer.dto.request.CustomerRequest;
import com.micro.customer.dto.response.CustomerResponse;
import com.micro.customer.entity.Customer;
import com.micro.customer.exception.CustomerNotFoundException;
import com.micro.customer.mapper.CustomerMapper;
import com.micro.customer.repository.CustomerRepository;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String createCustomer(CustomerRequest request) {

        var customer = customerRepository.save(customerMapper.toCustomer(request));

        return customer.getId();
    }

    public String updateCustomer(CustomerRequest request) {
        System.out.println(1244412);
        var customer = customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                String.format("Cannot update customer:: No customer with id:: %s  can be found", request.id())
        ));
        mergeCustomer(customer, request);

        customerRepository.save(customer);
        return customer.getId();
    }

    public List<CustomerResponse> getAllCustomer() {
        var customer = customerRepository.findAll();
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("No customer found");
        }
        return customer.stream()
                .map(customerMapper::fromCustomer)
                .toList();
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public CustomerResponse findById(String id) {
        return customerRepository.findById(id)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("No customer found with the provided ID: %s", id)));
    }

    public boolean existsById(String id) {
        return customerRepository.findById(id)
                .isPresent();
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }
}
