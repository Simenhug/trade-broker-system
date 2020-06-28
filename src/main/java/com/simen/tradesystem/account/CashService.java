package com.simen.tradesystem.account;

import com.simen.tradesystem.position.EquityPosition;
import com.simen.tradesystem.position.EquityPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashService {
    @Autowired
    private EquityPositionService equityService;
    @Autowired
    private CashRepository cashRepository;

    public double getTotalMarketValue(Cash cash) {
        double total = 0;
        for (EquityPosition equity : cash.getEquities()) {
            total += equityService.marketValue(equity);
        }
        return total;
    }
    public double getNetWorth(Cash cash){
        return cash.getBalance() + getTotalMarketValue(cash);
    }
}
