package com.hb.cda.thymeleafproject.repository;

import com.hb.cda.thymeleafproject.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductRepository extends JpaRepository<Product, String> {
    @Override
    Page<Product> findAll(Pageable pageable);

    Product findByName(String name);
}
