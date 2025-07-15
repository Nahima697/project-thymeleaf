package com.hb.cda.thymeleafproject.service;

import com.hb.cda.thymeleafproject.dto.AddProductDTO;
import com.hb.cda.thymeleafproject.dto.ProductInCartDTO;
import com.hb.cda.thymeleafproject.entity.Product;
import com.hb.cda.thymeleafproject.entity.ProductInCart;
import com.hb.cda.thymeleafproject.entity.User;
import com.hb.cda.thymeleafproject.mapper.ProductMapper;
import com.hb.cda.thymeleafproject.repository.ProductInCartRepository;
import com.hb.cda.thymeleafproject.repository.ProductRepository;
import com.hb.cda.thymeleafproject.service.impl.DatabaseCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseCartServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductInCartRepository productInCartRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private DatabaseCartService databaseCartService;

    private ProductInCart productInCart1;
    private ProductInCart productInCart2;
    private Product product1;
    private Product product2;
    private ProductInCartDTO dto1;
    private ProductInCartDTO dto2;
    private AddProductDTO addDto1;
    private AddProductDTO addDto2;
    private User mockUser;


    @BeforeEach
    void setUp() {
        // Simuler l'utilisateur courant
        mockUser = new User();
        mockUser.setId("42");

        // Simuler les entités en base
        productInCart1 = new ProductInCart();
        productInCart2 = new ProductInCart();

        product1 = new Product("prod1","test1",10.0,"prod",50);

        product2 = new Product("prod2","test2", 15.0, "desc2", 30);

        // Simuler les DTOs retournés après mapping
        dto1 = new ProductInCartDTO("Product1", 10.0, "desc1", 1);
        dto2 = new ProductInCartDTO("Product2", 20.0, "desc2", 2);

        //Simuler les AddProductDto
        addDto1 = new AddProductDTO("test1",10);
        addDto2 = new AddProductDTO( "test2", 11);

        addDto1.setId(product1.getId());
        addDto2.setId(product2.getId());

    }

    @Test
    void testGetTotal_shoudlReturnTotal() {
        // Préparer les données simulées
        List<ProductInCartDTO> productsInCart = List.of(dto1, dto2);

        DatabaseCartService spyService = Mockito.spy(databaseCartService);
        Mockito.doReturn(productsInCart).when(spyService).getCart();

        // Tester la méthode getTotal()
        double total = spyService.getTotal();

        assertEquals(10.0*1 + 20.0*2, total);
    }

    @Test
    void testGetCart_shouldReturnMappedProductsForCurrentUser() {
        DatabaseCartService spyService = Mockito.spy(databaseCartService);
        Mockito.doReturn(mockUser).when(spyService).getCurrentUser();

        // Simuler le repository et le mapper
        when(productInCartRepository.findByUserId("42")).thenReturn(List.of(productInCart1, productInCart2));
        when(productMapper.productToCartDto(productInCart1)).thenReturn(dto1);
        when(productMapper.productToCartDto(productInCart2)).thenReturn(dto2);

        // Act
        List<ProductInCartDTO> result = spyService.getCart();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals("Product2", result.get(1).getName());

        verify(productInCartRepository).findByUserId("42");
        verify(productMapper).productToCartDto(productInCart1);
        verify(productMapper).productToCartDto(productInCart2);
    }

    @Test
    void testAddProductToCart_shouldUpdateQuantityIfProductAlreadyExists() {
        DatabaseCartService spyService = Mockito.spy(databaseCartService);
        Mockito.doReturn(mockUser).when(spyService).getCurrentUser();
        when(productRepository.findById("prod1")).thenReturn(Optional.of(product1));
        when(productInCartRepository.findByUserIdAndProductId("42", "prod1")).thenReturn(Optional.of(productInCart1));
        spyService.addProductToCart(addDto1, 3);

        // Assert
        assertEquals(3, productInCart1.getQuantity());
        verify(productInCartRepository).save(productInCart1);
    }

    @Test
    void testAddProductToCart_shouldAddProductIfNotInCartYet() {
        DatabaseCartService spyService = Mockito.spy(databaseCartService);
        Mockito.doReturn(mockUser).when(spyService).getCurrentUser();

        // Simuler la récupération du produit product2 dans le repo
        when(productRepository.findById("prod2")).thenReturn(Optional.of(product2));

        // Simuler l'absence de ce produit dans le panier de l'utilisateur
        when(productInCartRepository.findByUserIdAndProductId("42", "prod2")).thenReturn(Optional.empty());

        // Appeler la méthode à tester
        spyService.addProductToCart(addDto2, 2);

        // Vérifier que productInCartRepository.save a été appelé avec un ProductInCart
        // qui contient bien product2, mockUser, et quantity 2
        verify(productInCartRepository).save(Mockito.argThat(cart ->
                cart.getProduct().equals(product2) &&
                        cart.getUser().equals(mockUser) &&
                        cart.getQuantity() == 2
        ));
    }

}
