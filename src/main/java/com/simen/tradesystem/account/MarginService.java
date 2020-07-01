package com.simen.tradesystem.account;

import com.simen.tradesystem.position.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quote.Quote;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

//be very careful, Margin and Position cannot both be saved to repository under any circumstances. Only save one
//at a time. If both are saved after a change is made, will create duplicated records
//remember when a position is created or destroyed, only to update margin.position-list, and only save the position
@Service
public class MarginService {

    @Autowired
    private MarginRepository marginRepository;
    @Autowired
    private OptionPositionService OPservice;
    @Autowired 
    private EquityPositionService EPservice;

    public double getTotalMarketValue(Margin margin) {
        double total = 0;
        for (EquityPosition position : margin.getEquities()) {
            total += EPservice.marketValue(position);
        }
        return total;
    }

    public void deposit(double amount, Margin margin) {
        double balance = margin.getBalance() + amount;
        margin.setBalance(balance);
        marginRepository.save(margin);
    }

    public void withdraw(double amount, Margin margin) throws IllegalArgumentException{
        if (amount > calculateMarginExcess(margin)) {
            throw new IllegalArgumentException("invalid withdraw amount. cannot withdraw more than margin excess");
        }
        double balance = margin.getBalance() - amount;
        margin.setBalance(balance);
        marginRepository.save(margin);
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
                EPservice.buy(quantity, position);
            }
        }
        marginRepository.save(margin);
        if (!exist) {
            EquityPosition position = new EquityPosition(quantity, symbol);
            position.setMargin(margin);
            EPservice.save(position);
            margin.getEquities().add(position);
        }
    }

    public void sellStock(String symbol, Integer quantity, Margin margin) throws IllegalArgumentException{
        boolean exist = false;
        List<EquityPosition> equities = margin.getEquities();
        double balance = margin.getBalance();
        Iterator<EquityPosition> iterator = equities.iterator();
        while (iterator.hasNext()){
            EquityPosition position = iterator.next();
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
                        EPservice.sell(quantity, position);
                    }
                } else {
                    //closing
                    EPservice.sell(quantity, position);
                    margin.setBalance(balance + quantity*Quote.getStockLastPrice(symbol));
                    if (position.getQuantity() == 0) {
                        equities.remove(position);
                        EPservice.delete(position);
                        break;
                    }
                }
            }
        }
        marginRepository.save(margin);
        if (!exist) {
            double shortValue = quantity*Quote.getStockLastPrice(symbol);
            if (shortValue > calculateBuyingPower(margin)) {
                throw new IllegalArgumentException("insufficient buying power for shorting");
            } else {
                margin.setBalance(balance + shortValue);
                EquityPosition position = new EquityPosition((-quantity), symbol);
                position.setMargin(margin);
                EPservice.save(position);
                margin.getEquities().add(position);
            }
        }
    }

    public void buyOption(String symbol, Integer quantity, Margin margin) {
        double buyValue = quantity*Quote.getOptionLastPrice(symbol)*100;
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
                OPservice.buy(quantity, position);
            }
        }
        marginRepository.save(margin);
        if (!exist) {
            OptionPosition position = new OptionPosition(quantity, symbol);
            position.setMargin(margin);
            OPservice.save(position);
            margin.getOptions().add(position);
        }
    }

    public void sellOption(String symbol, Integer quantity, Margin margin) {
        boolean exist = false;
        List<OptionPosition> options = margin.getOptions();
        double balance = margin.getBalance();
        Iterator<OptionPosition> iterator = options.iterator();
        while (iterator.hasNext()){
            OptionPosition position = iterator.next();
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                if (position.getQuantity() < quantity) {
                    throw new IllegalArgumentException("insufficient position. cannot short sell");
                } else {
                    OPservice.sell(quantity, position);
                    margin.setBalance(balance + quantity*Quote.getOptionLastPrice(symbol)*100);
                    if (position.getQuantity() == 0) {
                        options.remove(position);
                        OPservice.delete(position);
                        break;
                    }
                }
            }
        }
        //no writing options for now
        if (!exist) {
            throw new IllegalArgumentException("cannot short sell");
        }
        marginRepository.save(margin);
    }

    public double calculateMaintenanceRequirement(Margin margin) {
        double totalReq = 0;
        for (EquityPosition position : margin.getEquities()) {
            totalReq += EPservice.maintenanceRequirement(position);
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
