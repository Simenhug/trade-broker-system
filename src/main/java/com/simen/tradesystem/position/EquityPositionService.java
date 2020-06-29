package com.simen.tradesystem.position;

import com.simen.tradesystem.securities.Equity;
import com.simen.tradesystem.securities.EquityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quote.Quote;

@Service
public class EquityPositionService {
    @Autowired
    private EquityRepository repository;
    @Autowired
    private EquityPositionRepository positionRepository;

    public double marketValue(EquityPosition position) {
        return position.getQuantity()* Quote.getStockLastPrice(position.getSymbol());
    };

    public double maintenanceRequirement(EquityPosition position) {
        Equity equity = repository.findBySymbol(position.getSymbol());
        return marketValue(position)*equity.getMaintenanceRequirement();
    };

    public void buy(int amount, EquityPosition position) {
        int size = position.getQuantity() + amount;
        position.setQuantity(size);
        positionRepository.save(position);
    }

    public void sell(int amount, EquityPosition position) {
        int size = position.getQuantity() - amount;
        position.setQuantity(size);
        positionRepository.save(position);
    }

    public void save(EquityPosition position) {
        positionRepository.save(position);
    }

    public void delete(EquityPosition position) {
        positionRepository.delete(position);
    }
}
