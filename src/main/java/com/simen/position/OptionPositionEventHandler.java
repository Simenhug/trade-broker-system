package com.simen.position;

import com.simen.securities.Option;
import com.simen.securities.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class OptionPositionEventHandler {
    private final OptionRepository repository;

    @Autowired
    public OptionPositionEventHandler(OptionRepository repository) {
        this.repository = repository;
    }

    @HandleBeforeCreate
    @HandleBeforeSave
    public void setEquityBasedOnSymbol(OptionPosition position) {
        Option option = repository.findBySymbol(position.getSymbol());
        position.setOption(option);
        System.out.println("\n\n\n\n\n\n\nlet me know!!!!!!!!!!!!!!!!");
    }

}
