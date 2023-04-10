package pl.allegrov2.allegrov2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.assemblers.ProductAssembler;
import pl.allegrov2.allegrov2.data.helpers.ProductFilter;
import pl.allegrov2.allegrov2.services.product.ProductService;
import pl.allegrov2.allegrov2.validation.exceptions.NotFoundException;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductAssembler productAssembler;
    private final PagedResourcesAssembler<Product> pagedAssembler;

    @PostMapping("/admin/products")
    public ResponseEntity<EntityModel<Product>> addProduct(@RequestBody Product product){
        EntityModel<Product> model = productAssembler
                .toModel(productService.saveProduct(product));

        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .build();
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/products/{id}")
    public EntityModel<Product> getById(@PathVariable Long id){
        Product product = productService.getById(id)
                .orElseThrow(() -> new NotFoundException("No products with id == " + id));

        return productAssembler.toModel(product);
    }

    // TODO: sort
    @GetMapping("/products/filtered")
    public CollectionModel<EntityModel<Product>> getFilteredProducts(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "false") boolean onlyInStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ProductFilter filter = new ProductFilter(search, onlyInStock);
        Page<Product> pageProduct = productService.getPage(filter, PageRequest.of(page, size));

        return pagedAssembler.toModel(
                pageProduct,
                productAssembler
        );
    }
}
