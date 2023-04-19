package pl.allegrov2.allegrov2.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.id = ?1")
    Cart getCartOfUser(Long userId);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart getCartOfUser(String userEmail);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    void deleteCartItem(Long cartId, Long productId);
}
