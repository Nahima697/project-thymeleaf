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
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DatabaseCartService extends AbstractCartService {

    private final ProductInCartRepository productInCartRepository;
    public DatabaseCartService(ProductRepository productRepository,
                               ProductMapper productMapper,
                               ProductInCartRepository productInCartRepository
                               ) {
        super(productRepository, productMapper);
        this.productInCartRepository = productInCartRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        throw new RuntimeException("Utilisateur non authentifié");
    }

    @Override
    public List<ProductInCartDTO> getCart() {
        User user = getCurrentUser();
        return productInCartRepository.findByUserId(user.getId())
                .stream()
                .map(productMapper::productToCartDto)
                .collect(Collectors.toList());
    }



    @Override
    public void addProductToCart(AddProductDTO dto, int quantity) {
        User user = getCurrentUser();
        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        Optional<ProductInCart> existingOpt = productInCartRepository.findByUserIdAndProductId(user.getId(), product.getId());
        if (existingOpt.isPresent()) {
            ProductInCart existing = existingOpt.get();
            existing.setQuantity(existing.getQuantity() + quantity);
            productInCartRepository.save(existing);
        } else {
            ProductInCart newItem = new ProductInCart();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            productInCartRepository.save(newItem);
        }
    }

    @Override
    public void removeProduct(ProductInCartDTO productInCartDTO) {
        User user = getCurrentUser();
        productInCartRepository.findByUserId(user.getId()).stream()
                .filter(item -> item.getProduct().getId().equals(productInCartDTO.getProductId()))
                .findFirst()
                .ifPresent(productInCartRepository::delete);
    }

    @Transactional
    @Override
    public void validateCart(List<ProductInCartDTO> cartItems) {
        for (ProductInCartDTO dto : cartItems) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + dto.getProductId()));

            if (dto.getQuantity() > product.getStock()) {
                throw new ValidationException("Stock insuffisant pour : " + product.getName());
            }

            product.setStock(product.getStock() - dto.getQuantity());
            productRepository.save(product);
        }

        // Vider le panier utilisateur
        clearCart();
    }

    @Override
    public void clearCart() {
        User user = getCurrentUser();
        productInCartRepository.deleteByUserId(user.getId());
    }
}

