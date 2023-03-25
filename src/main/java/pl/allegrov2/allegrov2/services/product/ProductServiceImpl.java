package pl.allegrov2.allegrov2.services.product;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.repositories.ProductPaginatedRepository;
import pl.allegrov2.allegrov2.repositories.ProductRepository;

import java.util.Optional;


@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final ProductPaginatedRepository paginationRepo;

    public Optional<Product> getById(Long id){
        return productRepo.findById(id);
    }

    public Page<Product> getPage(Pageable pageable){
        return paginationRepo.findAll(pageable);
    }

    public Product saveProduct(Product product){
        return productRepo.save(product);
    }

    public void deleteById(Long id){
        productRepo.deleteById(id);
    }
}
