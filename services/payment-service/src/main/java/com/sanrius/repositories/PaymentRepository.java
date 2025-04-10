package com.sanrius.repositories;

import com.sanrius.dto.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>  {
}
