package com.simen.tradesystem.position;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource
public interface OptionPositionRepository extends PagingAndSortingRepository<OptionPosition, Long> {
    @RestResource(rel = "symbol-is", path = "symbolEquals")
    Page<OptionPosition> findBySymbol(@Param("symbol") String symbol, Pageable page);

    @RestResource(rel = "symbol-contains", path = "hasSymbol")
    Page<OptionPosition> findBySymbolContaining(@Param("symbol") String symbol, Pageable page);

    @Query("select o from OptionPosition o where o.cash.id = :id")
    List<OptionPosition> findCashPositions(@Param("id") Long id);

    @Query("select o from OptionPosition o where o.margin.id = :id")
    List<OptionPosition> findMarginPositions(@Param("id") Long id);
}
