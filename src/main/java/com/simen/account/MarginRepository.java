package com.simen.account;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MarginRepository extends PagingAndSortingRepository<Margin, Long> {
}
