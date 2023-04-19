package pl.allegrov2.allegrov2.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import pl.allegrov2.allegrov2.data.entities.Product;


public interface ProductPaginatedRepository extends PagingAndSortingRepository<Product, Long> {

    @Query("SELECT prod FROM Product prod WHERE prod.brandName LIKE %?1% OR prod.modelName LIKE %?1%")
    Page<Product> findBy(Pageable pageable, String searchString);

    @Query("SELECT prod FROM Product prod " +
            "WHERE (prod.brandName LIKE %:searchString% OR prod.modelName LIKE %:searchString%) " +
            "AND (:inStock = false OR prod.stock > 0)")
    Page<Product> findBy(Pageable pageable, String searchString, @Param("inStock") boolean inStock);
}
