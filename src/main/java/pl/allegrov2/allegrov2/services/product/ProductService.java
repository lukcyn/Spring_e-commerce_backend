package pl.allegrov2.allegrov2.services.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.helpers.ProductFilter;

import java.util.Optional;

public interface ProductService {

    Optional<Product> getById(Long id);

    Product saveProduct(Product product);

    void deleteById(Long id);

    Page<Product> getPage(Pageable pageable);

    Page<Product> getPage(ProductFilter filter, Pageable pageable);
}
