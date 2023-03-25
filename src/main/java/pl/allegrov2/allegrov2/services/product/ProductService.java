package pl.allegrov2.allegrov2.services.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.allegrov2.allegrov2.data.entities.Product;

import java.util.Optional;

public interface ProductService {

    Optional<Product> getById(Long id);

    Page<Product> getPage(Pageable pageable);

    Product saveProduct(Product product);

    void deleteById(Long id);
}
