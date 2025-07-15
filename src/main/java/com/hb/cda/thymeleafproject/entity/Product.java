package com.hb.cda.thymeleafproject.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy =GenerationType.UUID )
    private String id;
    private String name;
    private Double price;
    private String description;
    private Integer stock;
    @OneToMany(mappedBy = "product")
    private Set<ProductInCart> usersWithProductInCart = new HashSet<>();

    public Product(String name, Double price, String description, Integer stock) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }
    public Product(String id, String name, Double price, String description, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }
    public Product() {
    }




    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Set<ProductInCart> getUsersWithProductInCart() {
        return usersWithProductInCart;
    }

    public void setUsersWithProductInCart(Set<ProductInCart> usersWithProductInCart) {
        this.usersWithProductInCart = usersWithProductInCart;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

}
