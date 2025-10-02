package com.java.paymentservice.repository;

import com.java.paymentservice.Entity.Payment;
import org.hibernate.NonUniqueResultException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrderId(int orderId) throws NonUniqueResultException;
}
