package com.example.springboottutorial.repositories;

import com.example.springboottutorial.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepositories extends JpaRepository<Product, Long> {
    List<Product> findByCategories(String categories);

}
