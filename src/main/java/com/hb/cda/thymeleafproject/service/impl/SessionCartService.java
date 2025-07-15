package com.hb.cda.thymeleafproject.service.impl;

import com.hb.cda.thymeleafproject.dto.AddProductDTO;
import com.hb.cda.thymeleafproject.dto.ProductInCartDTO;
import com.hb.cda.thymeleafproject.entity.Product;
import com.hb.cda.thymeleafproject.entity.ProductInCart;
import com.hb.cda.thymeleafproject.entity.User;
import com.hb.cda.thymeleafproject.mapper.ProductMapper;
import com.hb.cda.thymeleafproject.repository.ProductInCartRepository;
import com.hb.cda.thymeleafproject.repository.ProductRepository;
import com.hb.cda.thymeleafproject.service.AbstractCartService;
import io.micrometer.core.instrument.config.validate.ValidationException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;
import java.util.stream.Collectors;

@Service
@SessionScope
public class SessionCartService extends AbstractCartService {

    private final HttpSession session;

    public SessionCartService(ProductRepository productRepository,
                              ProductMapper productMapper,
                              HttpSession session) {
        super(productRepository, productMapper);
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ProductInCartDTO> getCartMap() {
        Map<String, ProductInCartDTO> cart = (Map<String, ProductInCartDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @Override
    public List<ProductInCartDTO> getCart() {
        return new ArrayList<>(getCartMap().values());
    }

    @Override
    public void removeProduct(ProductInCartDTO productInCartDTO) {

        ProductInCartDTO productInCartInMap = getCartMap().get(productInCartDTO.getProductId());
        if(productInCartInMap.getQuantity() > 1) {
            productInCartInMap.setQuantity(productInCartInMap.getQuantity() - 1);
        }
        else {
            getCartMap().remove(productInCartDTO.getProductId());
        }
    }

    @Override
    public void addProductToCart(AddProductDTO dto, int quantity) {
        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        Map<String, ProductInCartDTO> cart = getCartMap();

        ProductInCartDTO productInCartDTO = cart.get(product.getId());

        if (productInCartDTO != null) {
            productInCartDTO.setQuantity(productInCartDTO.getQuantity() + quantity);
        } else {
            ProductInCart productInCart = new ProductInCart();
            productInCart.setProduct(product);
            productInCart.setQuantity(quantity);

            ProductInCartDTO item = productMapper.productToCartDto(productInCart);
            cart.put(product.getId(), item);
        }
    }


    @Override
    public void validateCart(List<ProductInCartDTO> dtos) {
        for (ProductInCartDTO item : dtos) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + item.getProductId()));

            if (item.getQuantity() > product.getStock()) {
                throw new RuntimeException("Pas assez de stock pour " + product.getName());
            }
        }

        for (ProductInCartDTO item : dtos) {
            Product product = productRepository.findById(item.getProductId()).get();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
        getCartMap().clear();
    }

    @Override
    public void clearCart() {
        getCartMap().clear();
    }
}

