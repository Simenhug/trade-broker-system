package com.simen.position;

import com.simen.securities.Equity;
import com.simen.securities.EquityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quote.Quote;

@Service
public class EquityPositionService {
    @Autowired
    private EquityRepository repository;

    public double marketValue(EquityPosition position) {
        return position.getQuantity()* Quote.getStockLastPrice(position.getSymbol());
    };

    public double maintenanceRequirement(EquityPosition position) {
        Equity equity = repository.findBySymbol(position.getSymbol());
        return marketValue(position)*equity.getMaintenanceRequirement();
    };
}
