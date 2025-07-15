package com.hb.cda.thymeleafproject.repository;

import com.hb.cda.thymeleafproject.entity.ProductInCart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ProductInCartRepository extends JpaRepository<ProductInCart, String> {
List<ProductInCart> findByProductId(String productId);

List<ProductInCart> findByUserId(String id);

    Optional<ProductInCart> findByUserIdAndProductId(String userId, String productId);

    void deleteByUserId(String id);
}
