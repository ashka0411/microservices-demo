package com.java.orderservice.advice;

import com.java.orderservice.dto.CustomErrorResponse;
import com.java.orderservice.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderRestControllerAdvice {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException(OrderNotFoundException ex){
        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
                .errorCode("Order Not Found")
                .errorMessage(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.badRequest().body(customErrorResponse);
    }

}
