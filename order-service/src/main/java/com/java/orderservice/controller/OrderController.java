package com.java.orderservice.controller;

import com.java.orderservice.common.TransactionRequest;
import com.java.orderservice.common.TransactionResponse;
import com.java.orderservice.exception.OrderNotFoundException;
import com.java.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/order")
@EnableCaching
public class OrderController {

    @Autowired
    private OrderService orderService;

    private int count = 1;

    @PostMapping("/bookOrder")
    public TransactionResponse bookOrder(@RequestBody TransactionRequest transactionRequest, @RequestHeader("loggedInUser") String userName){
        System.out.println("Logged in User : " + userName);
        return orderService.saveOrder(transactionRequest);
    }

    @GetMapping("/{orderId}")
    //@CircuitBreaker(name = "orderService", fallbackMethod = "getOrdersFallback")
    //@Retry(name = "orderService", fallbackMethod = "getOrdersFallback")
    @Cacheable(key = "#orderId", value = "Order", unless = "#orderId > 100")
    public TransactionResponse getOrder(@PathVariable int orderId) throws OrderNotFoundException {
        //System.out.println("Retry count " + count++ + " at " + new Date());
        return orderService.getOrder(orderId);
    }


    public TransactionResponse getOrdersFallback(Exception e){
        return orderService.getAllOrder();
    }
}
