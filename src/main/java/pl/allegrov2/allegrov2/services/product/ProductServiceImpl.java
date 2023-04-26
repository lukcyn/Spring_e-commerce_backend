package pl.allegrov2.allegrov2.services.product;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.helpers.ProductFilter;
import pl.allegrov2.allegrov2.repositories.ProductPaginatedRepository;
import pl.allegrov2.allegrov2.repositories.ProductRepository;
import pl.allegrov2.allegrov2.validation.exceptions.NotFoundException;

import java.util.Map;


@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final ProductPaginatedRepository paginationRepo;

    public Product getById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("No product with id " + id + " found"));
    }

    public Page<Product> getPage(Pageable pageable) {
        return paginationRepo.findAll(pageable);
    }

    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    public void deleteById(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public Page<Product> getPage(ProductFilter filter, Pageable pageable) {
        return paginationRepo.findBy(
                pageable,
                filter.getSearchString().orElse(""),
                filter.getOnlyInStock().orElse(false)
        );
    }

    @Override
    @Transactional
    public void decreaseQuantity(Map<Product, Integer> productsWithQuantities) {
        for (Map.Entry<Product, Integer> entry : productsWithQuantities.entrySet()) {
            entry.getKey().decreaseStock(entry.getValue());
            productRepo.saveAll(productsWithQuantities.keySet());
        }
    }
}