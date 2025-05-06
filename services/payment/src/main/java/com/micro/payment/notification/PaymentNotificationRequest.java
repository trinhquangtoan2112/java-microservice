package com.micro.payment.notification;

import java.math.BigDecimal;

import com.micro.payment.Payment.PaymentMethod;

public record PaymentNotificationRequest(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstname,
        String customerLastname,
        String customerEmail
        ) {

}
