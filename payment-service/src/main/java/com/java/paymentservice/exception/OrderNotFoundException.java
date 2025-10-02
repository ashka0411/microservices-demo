package com.java.paymentservice.exception;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException(String message){
        super(message);
    }
}
