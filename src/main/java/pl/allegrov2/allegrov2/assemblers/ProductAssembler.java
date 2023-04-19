package pl.allegrov2.allegrov2.assemblers;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.controllers.ProductController;
import pl.allegrov2.allegrov2.data.entities.Product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public @NotNull EntityModel<Product> toModel(@NotNull Product entity) {
        return EntityModel.of(entity,
            linkTo(methodOn(ProductController.class).getById(entity.getId())).withSelfRel()
        );
    }
}
