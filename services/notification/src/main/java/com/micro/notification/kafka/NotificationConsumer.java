package com.micro.notification.kafka;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.micro.notification.email.EmailService;
import com.micro.notification.kafka.order.OrderConfirmation;
import com.micro.notification.kafka.payment.PaymentConfirmation;
import com.micro.notification.notification.Notification;
import com.micro.notification.notification.NotificationRepository;
import com.micro.notification.notification.NotificationType;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info("Payment success notification received: {}", paymentConfirmation);
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .build()
        );
        String customerName = String.format("%s %s", paymentConfirmation.customerFirstname(), paymentConfirmation.customerLastname());
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference()
        );

    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info("Payment success notification received: {}", orderConfirmation);
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()
        );

        String customerName = String.format("%s %s", orderConfirmation.customer().firstname(), orderConfirmation.customer().lastname());
        emailService.sendOrderEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
        //todo send email
    }
}
