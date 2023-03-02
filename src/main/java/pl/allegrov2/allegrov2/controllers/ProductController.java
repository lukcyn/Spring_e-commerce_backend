package pl.allegrov2.allegrov2.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.helpers.assemblers.ProductAssembler;
import pl.allegrov2.allegrov2.services.ProductService;

@Controller
@RequestMapping("api")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductAssembler productAssembler;

    @GetMapping("/products/{id}")
    @ResponseBody
    public EntityModel<Product> one(@PathVariable Long id){
        Product product = productService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("No products with id " + id));

        return productAssembler.toModel(product);
    }

    //todo sort and filter
    @GetMapping(path ="/products", params = {"page", "size"})
    @ResponseBody
    public Page<Product> getPage(@RequestParam("page") int page,
                                 @RequestParam("size") int size) {

        return productService.getAll(PageRequest.of(page, size));
    }

    @PostMapping("/admin")
    public ResponseEntity<?> addProduct(@RequestBody Product product){
        productService.saveProduct(product);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
