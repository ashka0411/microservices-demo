package com.java.paymentservice.controller;

import com.java.paymentservice.Entity.Payment;
import com.java.paymentservice.exception.OrderNotFoundException;
import com.java.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/doPayment")
    public Payment doPayment(@RequestBody Payment payment) {
        return paymentService.doPayment(payment);
    }

    @GetMapping("/{orderId}")
    public Payment findPaymentHistoryByOrderId(@PathVariable int orderId) throws OrderNotFoundException {
        return paymentService.findPaymentHistoryByOrderId(orderId);
    }
}
