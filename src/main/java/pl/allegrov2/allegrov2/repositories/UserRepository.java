package pl.allegrov2.allegrov2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.allegrov2.allegrov2.data.entities.AppUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    @Query("SELECT u.id FROM AppUser u WHERE u.email = ?1")
    Optional<Long> findUserIdByUsername(String username);
}