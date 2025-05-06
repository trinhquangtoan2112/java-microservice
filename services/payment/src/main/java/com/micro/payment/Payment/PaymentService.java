package com.micro.payment.Payment;

import org.springframework.stereotype.Service;

import com.micro.payment.notification.NotificationProducer;
import com.micro.payment.notification.PaymentNotificationRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest paymentRequest) {
        var payment = paymentRepository.save(paymentMapper.toPayment(paymentRequest));
        notificationProducer.sendNotification(
                new PaymentNotificationRequest(paymentRequest.orderReference(), paymentRequest.amount(), paymentRequest.paymentMethod(), paymentRequest.customer().firstname(), paymentRequest.customer().lastname(), paymentRequest.customer().email())
        );
        return payment.getId();
    }
}
