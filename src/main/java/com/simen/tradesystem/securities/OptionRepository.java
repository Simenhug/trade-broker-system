package com.simen.tradesystem.securities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface OptionRepository extends PagingAndSortingRepository<Options, Long> {
    @RestResource(rel = "symbol-is", path = "symbolEquals")
    Options findBySymbol(@Param("symbol") String symbol);

    @RestResource(rel = "symbol-contains", path = "hasSymbol")
    Page<Options> findBySymbolContaining(@Param("symbol") String symbol, Pageable page);
}
