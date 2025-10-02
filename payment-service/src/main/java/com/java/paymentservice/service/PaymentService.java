package com.java.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.paymentservice.Entity.Payment;
import com.java.paymentservice.exception.OrderNotFoundException;
import com.java.paymentservice.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private Logger log = LoggerFactory.getLogger(PaymentService.class);

    public Payment doPayment(Payment payment) {

        try {
            log.info("Payment Service - doPayment : {}", new ObjectMapper().writeValueAsString(payment));
            payment.setPaymentStatus(paymentProcessing());
            payment.setTransactionId(UUID.randomUUID().toString());

            return paymentRepository.save(payment);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String paymentProcessing() {
        return new Random().nextBoolean() ? "Success" : "Failure";
    }

    public Payment findPaymentHistoryByOrderId(int orderId) throws OrderNotFoundException {
        Optional<Payment> payment = null;
        Payment pay = null;
        try {
            payment = paymentRepository.findByOrderId(orderId);
            pay = payment.get();
            log.info("Payment Service - doPayment : {}", new ObjectMapper().writeValueAsString(payment));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            throw new OrderNotFoundException("Order not Found");
        }
        return pay;
    }
}
