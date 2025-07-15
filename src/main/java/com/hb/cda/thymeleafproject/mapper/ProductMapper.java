package com.hb.cda.thymeleafproject.mapper;

import com.hb.cda.thymeleafproject.dto.AddProductDTO;
import com.hb.cda.thymeleafproject.dto.ProductInCartDTO;
import com.hb.cda.thymeleafproject.entity.Product;
import com.hb.cda.thymeleafproject.entity.ProductInCart;
import com.hb.cda.thymeleafproject.entity.User;
import org.springframework.stereotype.Component;


@Component
public class ProductMapper {

    public AddProductDTO toDto(Product product) {
        AddProductDTO dto = new AddProductDTO();
        dto.setId(product.getId());
        dto.setStock(product.getStock());
        dto.setQuantity(1);
        return dto;
    }

    public ProductInCartDTO productToCartDto(ProductInCart productInCart) {
        ProductInCartDTO dto = new ProductInCartDTO();
        dto.setProductId(productInCart.getProduct().getId());
        dto.setName(productInCart.getProduct().getName());
        dto.setPrice(productInCart.getProduct().getPrice());
        dto.setQuantity(productInCart.getQuantity());
        return dto;
    }

    public ProductInCart cartDtoToProduct(ProductInCartDTO dto, Product product, User user) {
        ProductInCart productInCart = new ProductInCart();
        productInCart.setProduct(product);
        productInCart.setQuantity(dto.getQuantity());
        productInCart.setUser(user);
        return productInCart;
    }



    public  Product toEntity(AddProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setStock(dto.getStock());
        return product;
    }
}
