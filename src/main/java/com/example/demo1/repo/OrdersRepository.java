package com.example.demo1.repo;

import com.example.demo1.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findByRazorpayOrderId(String razorpayId);
}
