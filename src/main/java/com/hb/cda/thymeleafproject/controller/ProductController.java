package com.hb.cda.thymeleafproject.controller;

import com.hb.cda.thymeleafproject.dto.AddProductDTO;
import com.hb.cda.thymeleafproject.dto.ProductInCartDTO;
import com.hb.cda.thymeleafproject.entity.Product;
import com.hb.cda.thymeleafproject.entity.User;
import com.hb.cda.thymeleafproject.repository.ProductRepository;
import com.hb.cda.thymeleafproject.service.CartFacade;
import io.micrometer.core.instrument.config.validate.ValidationException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final CartFacade cartFacade;

    public ProductController(ProductRepository productRepository, CartFacade cartFacade) {
        this.productRepository = productRepository;
        this.cartFacade = cartFacade;
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(defaultValue = "1") int pageNumber,@AuthenticationPrincipal User user) {
        model.addAttribute("productPage", productRepository.findAll(PageRequest.of(pageNumber - 1, 5)));
        model.addAttribute("pageNumber", pageNumber);

        if (user != null) {
            model.addAttribute("user", user);
        }

        return "product-list";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        model.addAttribute("item", product);
        return "product-detail";

    }

    @PostMapping("/add-product")
    public String addProductToCart(@Valid @ModelAttribute("product") AddProductDTO dto,
                                   BindingResult bindingResult,
                                   @AuthenticationPrincipal User user,
                                   Model model,@RequestParam(defaultValue = "1") int pageNumber) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productPage", productRepository.findAll(PageRequest.of(pageNumber - 1, 5)));
            model.addAttribute("pageNumber", pageNumber);
            model.addAttribute("user", user);
            model.addAttribute("errors", bindingResult.getFieldErrors());
            return "product-list";
        }

        cartFacade.addProductToCart(dto, dto.getQuantity(), user);
        return "redirect:/product/list";
    }


    @GetMapping("/cart")
    public String cart(@AuthenticationPrincipal User user, Model model) {
        List<ProductInCartDTO> productInCartDTOS=  cartFacade.getCart(user);
        model.addAttribute("productInCart",productInCartDTOS );
        double totalCart = cartFacade.getTotal(user);
        model.addAttribute("totalCart",totalCart);
        return "product-cart";
    }

    @PostMapping("/validate-cart")
    public String validateCart(@AuthenticationPrincipal User user, Model model) {
        try {
            List<ProductInCartDTO> dtos = cartFacade.getCart(user);
            cartFacade.validateCart(user, dtos);
            return "redirect:/product/list";
        } catch (ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<ProductInCartDTO> productInCart = cartFacade.getCart(user);
            model.addAttribute("productInCart", productInCart);
            model.addAttribute("totalCart", cartFacade.getTotal(user));
            return "product-cart";
        }
    }

    @PostMapping("/remove-product")
    public String removeProductFromCart(@RequestParam("id") String productId,
                                        @AuthenticationPrincipal User user) {
        Product product = productRepository.findById(productId).orElseThrow();
        ProductInCartDTO dto = new ProductInCartDTO();
        dto.setProductId(product.getId());

        cartFacade.removeProduct(dto,user);
        return "redirect:/product/cart";
    }





}



