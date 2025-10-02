package com.java.cloudgateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/orderFallBack")
    public Mono<String> orderServiceFallback() {
        return Mono.just("Order service is down");
    }

    @RequestMapping("/paymentFallBack")
    public Mono<String> paymentServiceFallback() {
        return Mono.just("Payment service is down");
    }
}
