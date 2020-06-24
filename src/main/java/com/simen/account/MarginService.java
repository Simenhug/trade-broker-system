package com.simen.account;

import com.simen.position.EquityPosition;
import com.simen.position.EquityPositionService;
import com.simen.position.OptionPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quote.Quote;

import java.util.List;

@Service
public class MarginService {
    @Autowired
    private EquityPositionService equityService;
    @Autowired
    private MarginRepository marginRepository;

    public double getTotalMarketValue(Margin margin) {
        double total = 0;
        for (EquityPosition position : margin.getEquities()) {
            total += equityService.marketValue(position);
        }
        return total;
    }

    public double getNetWorth(Margin margin){
        return margin.getBalance() + getTotalMarketValue(margin);
    }

    public void buyStock(String symbol, Integer quantity, Margin margin) throws IllegalArgumentException{
        double buyValue = quantity* Quote.getStockLastPrice(symbol);
        if (buyValue > calculateBuyingPower(margin)) {
            throw new IllegalArgumentException("insufficient buying power");
        }
        margin.setBalance(margin.getBalance() - buyValue);
        boolean exist = false;
        for (EquityPosition position : margin.getEquities()) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                position.buy(quantity);
            }
        }
        if (!exist) {
            EquityPosition position = new EquityPosition(quantity, symbol);
            margin.getEquities().add(position);
        }
        marginRepository.save(margin);
    }

    public void sellStock(String symbol, Integer quantity, Margin margin) throws IllegalArgumentException{
        boolean exist = false;
        List<EquityPosition> equities = margin.getEquities();
        double balance = margin.getBalance();
        for (EquityPosition position : equities) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                //shorting
                if (position.getQuantity() < quantity) {
                    int shortAmt = position.getQuantity() - quantity;
                    double shortValue = shortAmt*Quote.getStockLastPrice(symbol);
                    if (shortValue > calculateBuyingPower(margin)) {
                        throw new IllegalArgumentException("insufficient buying power for shorting");
                    } else {
                        margin.setBalance(balance + shortValue);
                        position.sell(quantity);
                    }
                } else {
                    //closing
                    position.sell(quantity);
                    margin.setBalance(balance + quantity*Quote.getStockLastPrice(symbol));
                    if (position.getQuantity() == 0) {
                        equities.remove(position);
                    }
                }
            }
        }
        if (!exist) {
            double shortValue = quantity*Quote.getStockLastPrice(symbol);
            if (shortValue > calculateBuyingPower(margin)) {
                throw new IllegalArgumentException("insufficient buying power for shorting");
            } else {
                margin.setBalance(balance - shortValue);
                EquityPosition position = new EquityPosition((0-quantity), symbol);
                equities.add(position);
            }
        }
        marginRepository.save(margin);
    }

    public void buyOption(String symbol, Integer quantity, Margin margin) {
        double buyValue = quantity*Quote.getOptionLastPrice(symbol);
        List<OptionPosition> options = margin.getOptions();
        double balance = margin.getBalance();
        if (buyValue > balance) {
            throw new IllegalArgumentException("insufficient cash balance");
        }
        margin.setBalance(balance - buyValue);
        boolean exist = false;
        for (OptionPosition position : options) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                position.buy(quantity);
            }
        }
        if (!exist) {
            OptionPosition position = new OptionPosition(quantity, symbol);
            options.add(position);
        }
        marginRepository.save(margin);
    }

    public void sellOption(String symbol, Integer quantity, Margin margin) {
        boolean exist = false;
        List<OptionPosition> options = margin.getOptions();
        double balance = margin.getBalance();
        for (OptionPosition position : options) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                if (position.getQuantity() < quantity) {
                    throw new IllegalArgumentException("insufficient position. cannot short sell");
                } else {
                    position.sell(quantity);
                    margin.setBalance(balance + quantity*Quote.getOptionLastPrice(symbol));
                    if (position.getQuantity() == 0) {
                        options.remove(position);
                    }
                }
            }
        }
        marginRepository.save(margin);
        //no writing options for now
        if (!exist) {
            throw new IllegalArgumentException("cannot short sell");
        }
    }

    public double calculateMaintenanceRequirement(Margin margin) {
        double totalReq = 0;
        for (EquityPosition position : margin.getEquities()) {
            totalReq += equityService.maintenanceRequirement(position);
        }
        return totalReq;
    }

    public double calculateMarginExcess(Margin margin) {
        return getNetWorth(margin) - calculateMaintenanceRequirement(margin);
    }

    public double calculateBuyingPower(Margin margin) {
        return calculateMarginExcess(margin)*2;
    }
}
