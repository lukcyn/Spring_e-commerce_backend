package pl.allegrov2.allegrov2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.allegrov2.allegrov2.data.entities.cart.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
