package pl.allegrov2.allegrov2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.id = ?1 AND o.user.id = ?2")
    Optional<Order> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.email = ?1")
    List<Order> findByUserEmail(String email);

    @Query("SELECT o FROM Order o WHERE o.user.id = ?1")
    List<Order> findByUserId(Long id);
}
