package com.micro.customer.dto.response;

import com.micro.customer.entity.Address;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
        ) {

}
