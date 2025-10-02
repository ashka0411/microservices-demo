package com.java.paymentservice.advice;

import com.java.paymentservice.dto.CustomErrorResponse;
import com.java.paymentservice.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentRestControllerAdvice {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException(OrderNotFoundException ex){
        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .errorCode("Order already exists")
                .errorMessage(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.badRequest().body(customErrorResponse);
    }

}
