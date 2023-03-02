package pl.allegrov2.allegrov2.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.repositories.IProductPaginatedRepository;
import pl.allegrov2.allegrov2.repositories.IProductRepository;

import java.util.Optional;


@Service
@AllArgsConstructor
public class ProductService {

    private final IProductRepository productRepo;
    private final IProductPaginatedRepository paginationRepo;

    public Optional<Product> getById(Long id){
        return productRepo.findById(id);
    }

    public Page<Product> getAll(Pageable pageable){
        return paginationRepo.findAll(pageable);
    }

    public void saveProduct(Product product){
        productRepo.save(product);
    }

    public void deleteById(Long id){
        productRepo.deleteById(id);
    }
}
