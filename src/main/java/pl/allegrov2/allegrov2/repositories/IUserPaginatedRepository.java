package pl.allegrov2.allegrov2.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.allegrov2.allegrov2.data.entities.AppUser;

public interface IUserPaginatedRepository extends PagingAndSortingRepository<AppUser, Long> {
}
