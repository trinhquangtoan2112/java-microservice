package com.micro.notification.email;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.micro.notification.kafka.order.Product;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(String destination, String customerName, BigDecimal amount, String orderReference) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
        helper.setFrom("cobantienganh1@gmail.com");
        final String templateName = EmailTemplates.PAYMENT_CONFIRMATION.getTemplate();
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("orderReference", orderReference);

        Context context = new Context();
        context.setVariables(variables);
        helper.setSubject(EmailTemplates.PAYMENT_CONFIRMATION.getSubject());

        try {
            String html = templateEngine.process(templateName, context);
            helper.setText(html, true);
            helper.setTo(destination);
            mailSender.send(message);
            log.info("Payment success email sent to {}", destination);
        } catch (Exception e) {
            throw new MessagingException("Error while processing email template", e);
        }
    }

    @Async
    public void sendOrderEmail(String destination, String customerName, BigDecimal amount, String orderReference, List<Product> products) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
        helper.setFrom("cobantienganh1@gmail.com");
        final String templateName = EmailTemplates.ORDER_CONFIRMATION.getTemplate();
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("totalAmount", amount);
        variables.put("orderReference", orderReference);
        variables.put("products", products);
        Context context = new Context();
        context.setVariables(variables);
        helper.setSubject(EmailTemplates.ORDER_CONFIRMATION.getSubject());

        try {
            String html = templateEngine.process(templateName, context);
            helper.setText(html, true);
            helper.setTo(destination);
            mailSender.send(message);
            log.info("Payment success email sent to {}", destination);
        } catch (Exception e) {
            throw new MessagingException("Error while processing email template", e);
        }
    }
}
