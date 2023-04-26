package pl.allegrov2.allegrov2.assemblers;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.controllers.OrderController;
import pl.allegrov2.allegrov2.data.entities.order.Order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class OrderAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public @NotNull EntityModel<Order> toModel(@NotNull Order entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(OrderController.class).getOrder("(authHeader)", entity.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getOrders("(authHeader)")).withRel("orders")
        );
    }

    @Override
    public @NotNull CollectionModel<EntityModel<Order>> toCollectionModel(@NotNull Iterable<? extends Order> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities).add(
                linkTo(methodOn(OrderController.class).getOrders("(authHeader)")).withSelfRel()
        );
    }
}
