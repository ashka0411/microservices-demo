package com.java.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.orderservice.common.Payment;
import com.java.orderservice.common.TransactionRequest;
import com.java.orderservice.common.TransactionResponse;
import com.java.orderservice.entity.Order;
import com.java.orderservice.exception.OrderNotFoundException;
import com.java.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

    private Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${microservice.payment-service.endpoints.endpoint.uri}")
    String paymentEndpoint;

    public TransactionResponse saveOrder(TransactionRequest transactionRequest) {

        try {
            log.info("Order service - saveOrder : {}", new ObjectMapper().writeValueAsString(transactionRequest));

            String response = "";
            Order order = transactionRequest.getOrder();
            Payment payment = transactionRequest.getPayment();
            payment.setOrderId(order.getId());
            payment.setAmount(order.getPrice());

            Payment paymentResponse = restTemplate.postForObject(paymentEndpoint, payment, Payment.class);
            response = paymentResponse.getPaymentStatus().equals("Success") ? "Payment processed and Order placed" : "Payment failed";

            log.info("Order Service - saveOrder - Payment Service called: {}", new ObjectMapper().writeValueAsString(response));
            orderRepository.save(order);
            return new TransactionResponse(order, payment.getAmount(), paymentResponse.getTransactionId(), response);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public TransactionResponse getOrder(int orderId) throws OrderNotFoundException {
        log.info("In getOrder method service class");
        String response = "";
        Order order = orderRepository.findById(orderId).orElse(null);
        Payment paymentResponse = null;
        try {
            paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/" + orderId, Payment.class);
        } catch (HttpClientErrorException e) {
            throw new OrderNotFoundException("Order Not Found");
        }
        return new TransactionResponse(order, paymentResponse.getAmount(), paymentResponse.getTransactionId(), response);
    }

    public TransactionResponse getAllOrder() {
        List<Order> order = orderRepository.findAll();
        return new TransactionResponse(order.get(0), 5000, "transactionId", null);
    }
}
