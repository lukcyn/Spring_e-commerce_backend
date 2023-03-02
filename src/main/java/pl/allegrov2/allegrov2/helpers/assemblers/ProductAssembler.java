package pl.allegrov2.allegrov2.helpers.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.data.entities.Product;

@Component
public class ProductAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    //todo add links
    @Override
    public EntityModel<Product> toModel(Product entity) {
        return EntityModel.of(entity


        );
    }
}
