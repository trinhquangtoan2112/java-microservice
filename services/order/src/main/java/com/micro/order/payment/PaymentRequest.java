package com.micro.order.payment;

import java.math.BigDecimal;

import com.micro.order.customer.CustomerResponse;
import com.micro.order.order.PaymentMethod;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
        ) {

}
