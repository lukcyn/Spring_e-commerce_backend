package pl.allegrov2.allegrov2.assemblers;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.controllers.CartController;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;
import pl.allegrov2.allegrov2.data.entities.cart.CartItem;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class CartAssembler implements RepresentationModelAssembler<Cart, EntityModel<Cart>> {

    private final ProductAssembler productAssembler;
    private final CartItemAssembler cartItemAssembler;

    @Override
    public EntityModel<Cart> toModel(Cart cart) {

        for(CartItem item: cart.getCartItems()){
            EntityModel<CartItem> cartItemEntityModel = cartItemAssembler.toModel(item);
            item.add(cartItemEntityModel.getLinks());
        }

        return EntityModel.of(cart,
                linkTo(methodOn(CartController.class).getCart("(authHeader)")).withSelfRel()
        );
    }
}
