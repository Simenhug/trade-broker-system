package com.simen.tradesystem.position;

import com.simen.tradesystem.securities.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface OptionPositionRepository extends PagingAndSortingRepository<OptionPosition, Long> {
    @RestResource(rel = "symbol-is", path = "symbolEquals")
    Page<OptionPosition> findBySymbol(@Param("symbol") String symbol, Pageable page);

    @RestResource(rel = "symbol-contains", path = "hasSymbol")
    Page<OptionPosition> findBySymbolContaining(@Param("symbol") String symbol, Pageable page);
}
