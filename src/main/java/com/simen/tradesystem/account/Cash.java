package com.simen.tradesystem.account;

import com.simen.tradesystem.core.BaseEntity;
import com.simen.tradesystem.position.EquityPosition;
import com.simen.tradesystem.position.OptionPosition;
import quote.Quote;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cash extends BaseEntity {
    private String username;
    private double balance;
    @OneToMany(cascade = CascadeType.ALL)
    private List<EquityPosition> equities = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<OptionPosition> options = new ArrayList<>();

    protected Cash() {
        super();
    }

    public Cash(String username) {
        this();
        this.username = username;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public double getBuyingPower() {
        return balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<EquityPosition> getEquities() {
        return equities;
    }

    public List<OptionPosition> getOptions() {
        return options;
    }

    public void buyStock(String symbol, Integer quantity) throws IllegalArgumentException{
        double buyValue = quantity*Quote.getStockLastPrice(symbol);
        if (buyValue > balance) {
            throw new IllegalArgumentException("insufficient cash balance");
        }
        balance -= buyValue;
        boolean exist = false;
        for (EquityPosition position : equities) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                position.buy(quantity);
            }
        }
        if (!exist) {
            EquityPosition position = new EquityPosition(quantity, symbol);
            equities.add(position);
            position.setCash(this);
        }
    }

    public void sellStock(String symbol, Integer quantity) throws IllegalArgumentException{
        boolean exist = false;
        for (EquityPosition position : equities) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                if (position.getQuantity() < quantity) {
                    throw new IllegalArgumentException("insufficient position. cannot short sell");
                } else {
                    position.sell(quantity);
                    balance += quantity*Quote.getStockLastPrice(symbol);
                    if (position.getQuantity() == 0) {
                        equities.remove(position);
                    }
                }
            }
        }
        if (!exist) {
            throw new IllegalArgumentException("cannot short sell");
        }
    }

    public void buyOption(String symbol, Integer quantity) {
        double buyValue = quantity*Quote.getOptionLastPrice(symbol);
        if (buyValue > balance) {
            throw new IllegalArgumentException("insufficient cash balance");
        }
        balance -= buyValue;
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
            position.setCash(this);
        }
    }

    public void sellOption(String symbol, Integer quantity) {
        boolean exist = false;
        for (OptionPosition position : options) {
            if (position.getSymbol().equals(symbol)) {
                exist = true;
                if (position.getQuantity() < quantity) {
                    throw new IllegalArgumentException("insufficient position. cannot short sell");
                } else {
                    position.sell(quantity);
                    balance += quantity*Quote.getOptionLastPrice(symbol);
                    if (position.getQuantity() == 0) {
                        equities.remove(position);
                    }
                }
            }
        }
        if (!exist) {
            throw new IllegalArgumentException("cannot short sell");
        }
    }
}
