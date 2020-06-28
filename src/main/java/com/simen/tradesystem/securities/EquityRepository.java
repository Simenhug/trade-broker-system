package com.simen.tradesystem.securities;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface EquityRepository extends PagingAndSortingRepository<Equity, Long> {
    @RestResource(rel = "symbol-is", path = "symbolEquals")
    Equity findBySymbol(@Param("symbol") String symbol);
}
