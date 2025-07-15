package com.hb.cda.thymeleafproject.service;

import com.hb.cda.thymeleafproject.dto.AddProductDTO;
import com.hb.cda.thymeleafproject.dto.ProductInCartDTO;
import com.hb.cda.thymeleafproject.entity.User;
import com.hb.cda.thymeleafproject.mapper.ProductMapper;
import com.hb.cda.thymeleafproject.repository.ProductInCartRepository;
import com.hb.cda.thymeleafproject.repository.ProductRepository;

import java.util.List;


public abstract class AbstractCartService implements CartServiceInterface {

    protected final ProductRepository productRepository;
    protected final ProductMapper productMapper;

    protected AbstractCartService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    // méthode commune - total
    @Override
    public double getTotal() {
        return getCart().stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
    }

    // méthode commune - liste les produits dans le panier
    @Override
    public abstract List<ProductInCartDTO> getCart();

    // méthode commune - vider le panier
    @Override
    public abstract void clearCart();

    @Override
    public abstract void addProductToCart(AddProductDTO dto, int quantity);

    @Override
    public abstract void removeProduct(ProductInCartDTO productInCartDTO);

    @Override
    public abstract void validateCart(List<ProductInCartDTO>productInCartDTOS);
}

