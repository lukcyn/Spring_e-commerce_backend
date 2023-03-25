package pl.allegrov2.allegrov2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.helpers.assemblers.ProductAssembler;
import pl.allegrov2.allegrov2.services.product.ProductService;
import pl.allegrov2.allegrov2.validation.exceptions.NotFoundException;

@Controller
@RequestMapping("api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductAssembler productAssembler;

    private final PagedResourcesAssembler<Product> pagedAssembler;

    @GetMapping("/products/{id}")
    @ResponseBody
    public EntityModel<Product> one(@PathVariable Long id){

        Product product = productService.getById(id)
                .orElseThrow(() -> new NotFoundException("No products with id == " + id));

        return productAssembler.toModel(product);
    }

    //todo sort and filter
    @GetMapping(path ="/products")
    @ResponseBody
    public CollectionModel<EntityModel<Product>> getAllPaginated(int page, int size) {
        return pagedAssembler.toModel(
                productService.getPage(PageRequest.of(page, size)),
                productAssembler
        );
    }

    @PostMapping("/admin/products")
    public ResponseEntity<EntityModel<Product>> addProduct(@RequestBody Product product){
        EntityModel<Product> model = productAssembler
                .toModel(productService.saveProduct(product));

        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .build();
    }

    @DeleteMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
