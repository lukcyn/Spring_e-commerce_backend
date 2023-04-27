package pl.allegrov2.allegrov2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import pl.allegrov2.allegrov2.assemblers.OrderAssembler;
import pl.allegrov2.allegrov2.data.entities.order.Order;
import pl.allegrov2.allegrov2.services.order.OrderService;
import pl.allegrov2.allegrov2.services.token.JwtService;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final JwtService jwtService;
    private final OrderService orderService;

    private final OrderAssembler orderAssembler;


    @GetMapping("/checkout")
    public EntityModel<Order> checkout(@RequestHeader("Authorization") String authHeader) {
        String username = jwtService.extractUsernameFromAuthHeader(authHeader);
        return orderAssembler.toModel(orderService.createOrderForCart(username));
    }

    @GetMapping("/{id}")
    public EntityModel<Order> getOrder(@RequestHeader("Authorization") String authHeader,
                                       @PathVariable long id){

        String username = jwtService.extractUsernameFromAuthHeader(authHeader);
        return orderAssembler.toModel(orderService.getUserOrderById(username, id));
    }

    @GetMapping
    public CollectionModel<EntityModel<Order>> getOrders(@RequestHeader("Authorization") String authHeader){
        String username = jwtService.extractUsernameFromAuthHeader(authHeader);
        return orderAssembler.toCollectionModel(orderService.getUserOrders(username));
    }
}
