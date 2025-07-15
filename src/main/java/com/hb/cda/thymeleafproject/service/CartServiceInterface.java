package com.hb.cda.thymeleafproject.service;

import com.hb.cda.thymeleafproject.dto.AddProductDTO;
import com.hb.cda.thymeleafproject.dto.ProductInCartDTO;
import com.hb.cda.thymeleafproject.entity.User;

import java.util.List;


public interface CartServiceInterface {
    void addProductToCart(AddProductDTO dto, int quantity);
    void removeProduct(ProductInCartDTO productInCartDTO);
    void validateCart(List<ProductInCartDTO> productInCartDTOList);
    double getTotal();
    List<ProductInCartDTO> getCart();
    void clearCart();

}
