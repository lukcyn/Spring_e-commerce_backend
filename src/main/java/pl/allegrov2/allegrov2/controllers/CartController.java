package pl.allegrov2.allegrov2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.allegrov2.allegrov2.assemblers.CartAssembler;
import pl.allegrov2.allegrov2.assemblers.ProductAssembler;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;
import pl.allegrov2.allegrov2.services.cart.CartService;
import pl.allegrov2.allegrov2.services.token.JwtService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtService jwtService;
    private final CartAssembler cartAssembler;
    private final ProductAssembler productAssembler;

    @PutMapping
    public ResponseEntity<Void> addProductToCart(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity){

        String username = jwtService.extractUsernameFromAuthHeader(authHeader);
        cartService.addToCart(username, productId, quantity);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public EntityModel<Cart> getCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        String username = jwtService.extractUsernameFromAuthHeader(authHeader);
        Cart cart = cartService.getCart(username);
        return cartAssembler.toModel(cart);
    }

    @DeleteMapping
    public EntityModel<Cart> removeItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                           @RequestParam Long productId,
                           @RequestParam(defaultValue = "1") int quantity){
        String username = jwtService.extractUsernameFromAuthHeader(authHeader);
        Cart cart = cartService.removeItem(username, productId, quantity);
        return cartAssembler.toModel(cart);
    }
}
