package com.simen.tradesystem.account;

import com.simen.tradesystem.position.EquityPosition;
import com.simen.tradesystem.position.EquityPositionService;
import com.simen.tradesystem.position.OptionPosition;
import com.simen.tradesystem.position.OptionPositionService;
import com.simen.tradesystem.securities.EquityRepository;
import com.simen.tradesystem.securities.OptionRepository;
import com.simen.tradesystem.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quote.Quote;

import java.util.List;

//be very careful, Cash and Position cannot both be saved to repository under any circumstances. Only save one
//at a time. If both are saved after a change is made, will create duplicated records
//remember when a position is created or destroyed, only to update cash.position-list, and only save the position
@Service
public class CashService {
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private OptionPositionService OPservice;
    @Autowired
    private EquityPositionService EPservice;
    @Autowired
    private EquityRepository equityRepository;
    @Autowired
    private OptionRepository optionRepository;

    public Cash findByUser(User user) {
        return cashRepository.findByUser(user);
    }

    public double getTotalMarketValue(Cash cash) {
        double total = 0;
        for (EquityPosition equity : cash.getEquities()) {
            total += EPservice.marketValue(equity);
        }
        return total;
    }
    public double getNetWorth(Cash cash){
        return cash.getBalance() + getTotalMarketValue(cash);
    }

    public void deposit(double amount, Cash cash) {
        double balance = cash.getBalance() + amount;
        cash.setBalance(balance);
        cashRepository.save(cash);
    }

    public void withdraw(double amount, Cash cash) throws IllegalArgumentException{
        if (amount > cash.getBalance()) {
            throw new IllegalArgumentException("insufficient fund");
        }
        double balance = cash.getBalance() - amount;
        cash.setBalance(balance);
        cashRepository.save(cash);
    }

    public void buyStock(String symbol, Integer quantity, Cash cash) throws IllegalArgumentException{
        double buyValue = quantity* Quote.getStockLastPrice(symbol);
        double balance = cash.getBalance();
        if (buyValue > balance) {
            throw new IllegalArgumentException("insufficient cash balance");
        }
        balance -= buyValue;
        cash.setBalance(balance);
        boolean exist = false;
        for (EquityPosition position : cash.getEquities()) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                EPservice.buy(quantity, position);
            }
        }
        cashRepository.save(cash);
        if (!exist) {
            EquityPosition position = new EquityPosition(quantity, symbol);
            position.setEquity(equityRepository.findBySymbol(symbol));
            position.setCash(cash);
            EPservice.save(position);
            cash.getEquities().add(position);
        }
    }

    public void sellStock(String symbol, Integer quantity, Cash cash) throws IllegalArgumentException{
        boolean exist = false;
        double balance = cash.getBalance();
        List<EquityPosition> equities = cash.getEquities();
        for (EquityPosition position : equities) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                if (position.getQuantity() < quantity) {
                    throw new IllegalArgumentException("insufficient position. cannot short sell");
                } else {
                    EPservice.sell(quantity, position);
                    balance += quantity*Quote.getStockLastPrice(symbol);
                    cash.setBalance(balance);
                    if (position.getQuantity() == 0) {
                        equities.remove(position);
                        EPservice.delete(position);
                        break;
                    }
                }
            }
        }
        if (!exist) {
            throw new IllegalArgumentException("cannot short sell");
        }
        cashRepository.save(cash);
    }

    public void buyOption(String symbol, Integer quantity, Cash cash) {
        double balance = cash.getBalance();
        double buyValue = quantity*Quote.getOptionLastPrice(symbol)*100;
        if (buyValue > balance) {
            throw new IllegalArgumentException("insufficient cash balance");
        }
        balance -= buyValue;
        cash.setBalance(balance);
        boolean exist = false;
        for (OptionPosition position : cash.getOptions()) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                OPservice.buy(quantity, position);
            }
        }
        cashRepository.save(cash);
        if (!exist) {
            OptionPosition position = new OptionPosition(quantity, symbol);
            position.setOptions(optionRepository.findBySymbol(symbol));
            position.setCash(cash);
            OPservice.save(position);
            cash.getOptions().add(position);
        }
    }

    public void sellOption(String symbol, Integer quantity, Cash cash) {
        boolean exist = false;
        double balance = cash.getBalance();
        List<OptionPosition> options = cash.getOptions();
        for (OptionPosition position : options) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                if (position.getQuantity() < quantity) {
                    throw new IllegalArgumentException("insufficient position. cannot short sell");
                } else {
                    OPservice.sell(quantity, position);
                    balance += quantity*Quote.getOptionLastPrice(symbol)*100;
                    cash.setBalance(balance);
                    if (position.getQuantity() == 0) {
                        OPservice.delete(position);
                        options.remove(position);
                        break;
                    }
                }
            }
        }
        if (!exist) {
            throw new IllegalArgumentException("cannot short sell");
        }
        cashRepository.save(cash);
    }
}
