package com.simen.tradesystem.account;

import com.simen.tradesystem.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CashRepository extends PagingAndSortingRepository<Cash, Long> {
    Cash findByUser(User user);
}
