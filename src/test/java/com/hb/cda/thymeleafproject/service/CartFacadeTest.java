package com.hb.cda.thymeleafproject.service;

import com.hb.cda.thymeleafproject.dto.AddProductDTO;
import com.hb.cda.thymeleafproject.dto.ProductInCartDTO;
import com.hb.cda.thymeleafproject.entity.User;
import com.hb.cda.thymeleafproject.service.impl.DatabaseCartService;
import com.hb.cda.thymeleafproject.service.impl.SessionCartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartFacadeTest {

    @Mock
    private DatabaseCartService databaseCartService;
    @Mock
    private SessionCartService sessionCartService;

    @InjectMocks
    private CartFacade cartFacade;

    @Test
    void testAddProductToCart_whenUserIsNull_shouldCallSessionService() {
        AddProductDTO dto = new AddProductDTO();
        cartFacade.addProductToCart(dto, 2, null);

        verify(sessionCartService).addProductToCart(dto, 2);
        verify(databaseCartService, never()).addProductToCart(any(), anyInt());
    }

    @Test
    void testAddProductToCart_whenUserExists_shouldCallDatabaseService() {
        AddProductDTO dto = new AddProductDTO();
        User user = new User();
        cartFacade.addProductToCart(dto, 2, user);
        verify(databaseCartService).addProductToCart(dto, 2);
        verify(sessionCartService, never()).addProductToCart(any(), anyInt());
    }

    @Test
    void testRemoveProductFromCart_whenUserIsNull_shouldCallSessionService() {
        ProductInCartDTO dto = new ProductInCartDTO();
        cartFacade.removeProduct(dto,null);
        verify(sessionCartService).removeProduct(dto);
        verify(databaseCartService, never()).removeProduct(any());
    }

    @Test
    void testRemoveProductFromCart_whenUserExists_shouldCallDatabaseService() {
        ProductInCartDTO dto = new ProductInCartDTO();
        User user = new User();
        cartFacade.removeProduct(dto, user);
        verify(databaseCartService).removeProduct(dto);
        verify(sessionCartService, never()).removeProduct(any());
    }

    @Test
    void testValidateCart_whenUserIsNull_shouldCallSessionService() {
        List<ProductInCartDTO> dtos = new ArrayList<>();
        cartFacade.validateCart(null, dtos);
        verify(sessionCartService).validateCart(dtos);
        verify(databaseCartService, never()).validateCart(any());
    }

    @Test
    void testValidateCart_whenUserExists_shouldCallDatabaseService() {
        List<ProductInCartDTO> dtos = new ArrayList<>();
        User user = new User();
        cartFacade.validateCart(user, dtos);
        verify(databaseCartService).validateCart(dtos);
        verify(sessionCartService, never()).validateCart(any());
    }

    @Test
    void testGetCart_whenUserIsNull_shouldCallSessionService() {
        List<ProductInCartDTO> dtos = new ArrayList<>();
        cartFacade.getCart(null);
        verify(sessionCartService).getCart();
        verify(databaseCartService, never()).getCart();
    }

    @Test
    void testGetCart_whenUserExists_shouldCallDatabaseService() {
        List<ProductInCartDTO> dtos = new ArrayList<>();
        User user = new User();
        cartFacade.getCart(user);
        verify(databaseCartService).getCart();
        verify(sessionCartService,never()).getCart();
    }

    @Test
    void testGetTotal_whenUserIsNull_shouldUseSessionService() {
        when(sessionCartService.getTotal()).thenReturn(42.0);

        double result = cartFacade.getTotal(null);

        verify(sessionCartService).getTotal();
        verify(databaseCartService, never()).getTotal();
        assertEquals(42.0, result);
    }

    @Test
    void testGetTotal_whenUserExists_shouldUseDatabaseService() {
        User user = new User();
        when(databaseCartService.getTotal()).thenReturn(99.99);

        double result = cartFacade.getTotal(user);

        verify(databaseCartService).getTotal();
        verify(sessionCartService, never()).getTotal();
        assertEquals(99.99, result);
    }

}
