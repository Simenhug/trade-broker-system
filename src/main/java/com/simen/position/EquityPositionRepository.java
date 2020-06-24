package com.simen.position;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EquityPositionRepository extends PagingAndSortingRepository<EquityPosition, Long> {
}
