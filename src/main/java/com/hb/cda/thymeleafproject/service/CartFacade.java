package com.hb.cda.thymeleafproject.service;

import com.hb.cda.thymeleafproject.dto.AddProductDTO;
import com.hb.cda.thymeleafproject.dto.ProductInCartDTO;
import com.hb.cda.thymeleafproject.entity.User;
import com.hb.cda.thymeleafproject.service.impl.DatabaseCartService;
import com.hb.cda.thymeleafproject.service.impl.SessionCartService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public class CartFacade {

    private final SessionCartService sessionCartService;
    private final DatabaseCartService databaseCartService;


    public CartFacade(SessionCartService sessionCartService,
                      DatabaseCartService databaseCartService) {
        this.sessionCartService = sessionCartService;
        this.databaseCartService = databaseCartService;
    }

    public List<ProductInCartDTO> getCart( User user) {
        if(user != null) {
           return databaseCartService.getCart();
        }
        else {
           return sessionCartService.getCart();
        }

    }

    public void addProductToCart(AddProductDTO dto,int quantity,User currentUser) {
        if (currentUser == null) {
            sessionCartService.addProductToCart(dto,quantity);
        } else {
            databaseCartService.addProductToCart(dto, quantity);
        }
    }

    public void removeProduct(ProductInCartDTO productInCartDTO,User currentUser) {
        if(currentUser != null) {
            databaseCartService.removeProduct(productInCartDTO);
        }
        else {
            sessionCartService.removeProduct(productInCartDTO);
        }
    }

    public void validateCart(User currentUser,List<ProductInCartDTO> cart) {
        if(currentUser != null) {
            databaseCartService.validateCart(cart);
        }
        else {
            sessionCartService.validateCart(cart);
        }
    }
    public double getTotal(User user) {
        if (user == null) {
            return sessionCartService.getTotal();
        } else {
            return databaseCartService.getTotal();
        }
    }
}
