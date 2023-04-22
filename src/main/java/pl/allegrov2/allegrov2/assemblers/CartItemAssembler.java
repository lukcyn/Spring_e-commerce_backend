package pl.allegrov2.allegrov2.assemblers;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.controllers.CartController;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.entities.cart.CartItem;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class CartItemAssembler implements RepresentationModelAssembler<CartItem, EntityModel<CartItem>> {

    private final ProductAssembler productAssembler;

    @Override
    public EntityModel<CartItem> toModel(CartItem item) {
        Product product = item.getProduct();
        EntityModel<Product> productModel = productAssembler.toModel(product);
        product.add(productModel.getLinks());

        return EntityModel.of(item,
                linkTo(methodOn(CartController.class).removeItem("(authHeader)", product.getId(), 1))
                        .withRel("Decrease quantity of items with id: " + product.getId() + " in cart"),
                linkTo(methodOn(CartController.class).addProductToCart("(authHeader)", product.getId(), 1))
                        .withRel("Increase quantity of item with id: " + product.getId() + " by given quantity.")
        );
    }
}
