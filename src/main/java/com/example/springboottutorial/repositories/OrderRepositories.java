package com.example.springboottutorial.repositories;

import com.example.springboottutorial.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositories extends JpaRepository<Order, Long> {
}
