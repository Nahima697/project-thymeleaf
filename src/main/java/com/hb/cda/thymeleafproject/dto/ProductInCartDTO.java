package com.hb.cda.thymeleafproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductInCartDTO {
    @NotNull
    private String productId;
    @NotBlank
    private String name;
    @NotNull
    private double price;
    @NotBlank
    private String description;
    @NotNull
    private int quantity;
    private double totalPrice;

    public ProductInCartDTO() {
    }
    public ProductInCartDTO( String name, double price, String description, int quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

//    public ProductInCartDTO(String productId,String name,double totalPrice, int quantity, double price) {
//        this.productId= productId;
//        this.name = name;
//        this.totalPrice = totalPrice;
//        this.quantity = quantity;
//        this.price = price;
//    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
