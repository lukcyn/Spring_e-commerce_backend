package pl.allegrov2.allegrov2.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.allegrov2.allegrov2.data.entities.AppUser;

@Repository
public interface UserPaginatedRepository extends PagingAndSortingRepository<AppUser, Long> {
}
