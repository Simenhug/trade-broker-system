package com.simen.tradesystem.position;

import com.simen.tradesystem.securities.Equity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface EquityPositionRepository extends PagingAndSortingRepository<EquityPosition, Long> {
    @RestResource(rel = "symbol-is", path = "symbolEquals")
    Page<EquityPosition> findBySymbol(@Param("symbol") String symbol, Pageable pageable);
}
