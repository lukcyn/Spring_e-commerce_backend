package pl.allegrov2.allegrov2.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.allegrov2.allegrov2.data.entities.Product;

public interface IProductPaginatedRepository extends PagingAndSortingRepository<Product, Long> {
}
