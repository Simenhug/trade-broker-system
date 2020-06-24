package com.simen.securities;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface OptionRepository extends PagingAndSortingRepository<Option, Long> {
    @RestResource(rel = "symbol-is", path = "hasSymbol")
    Option findBySymbol(@Param("symbol") String symbol);
}
