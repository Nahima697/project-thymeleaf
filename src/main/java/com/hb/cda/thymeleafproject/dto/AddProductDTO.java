package com.hb.cda.thymeleafproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddProductDTO {
    @NotBlank
    private String id;

    private Integer stock;

    @NotNull
    private Integer quantity = 1;

    public AddProductDTO() {}
    public AddProductDTO(String id, Integer stock) {
        this.id = id;
        this.stock = stock;
    }

    public AddProductDTO(  Integer stock, Integer quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
