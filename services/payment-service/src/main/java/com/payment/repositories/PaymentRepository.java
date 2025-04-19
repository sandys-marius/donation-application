package com.payment.repositories;

import com.payment.dto.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>  {
}
