package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService=cartService;
    }


    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication){
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.getCart(username));
    }


    @PostMapping("/items/{productId}")
    public ResponseEntity<Cart> addToCart(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            Authentication authentication
    ){
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.addToCart(username, productId, quantity));
    }


    @DeleteMapping
    public ResponseEntity<Cart> clearCart(Authentication authentication){
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.clearCart(username));
    }


    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Cart> removeFromCart(
            @PathVariable Long productId,
            Authentication authentication
    ){
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.removeFromCart(username, productId));
    }


    @PutMapping("/items/{productId}")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity,
            Authentication authentication
    ){
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.updateQuantity(username, productId, quantity));
    }





}
