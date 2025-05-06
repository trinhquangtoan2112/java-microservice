package com.micro.order.kafka;

import java.math.BigDecimal;
import java.util.List;

import com.micro.order.customer.CustomerResponse;
import com.micro.order.order.PaymentMethod;
import com.micro.order.product.PurchaseResponse;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
        ) {

}
