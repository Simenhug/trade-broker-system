package com.simen.position;

import com.simen.securities.Equity;
import com.simen.securities.EquityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class EquityPositionEventHandler {
    private final EquityRepository repository;

    @Autowired
    public EquityPositionEventHandler(EquityRepository repository) {
        this.repository = repository;
    }

    @HandleBeforeCreate
    @HandleBeforeSave
    public void setEquityBasedOnSymbol(EquityPosition equityPosition) {
        Equity equity = repository.findBySymbol(equityPosition.getSymbol());
        equityPosition.setEquity(equity);
        System.out.println("\n\n\n\n\n\n\nlet me know!!!!!!!!!!!!!!!!");
    }

}
