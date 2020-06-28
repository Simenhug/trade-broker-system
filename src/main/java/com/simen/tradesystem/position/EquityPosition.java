package com.simen.tradesystem.position;

import com.simen.tradesystem.account.Cash;
import com.simen.tradesystem.account.Margin;
import com.simen.tradesystem.core.BaseEntity;
import com.simen.tradesystem.securities.Equity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class EquityPosition extends BaseEntity {

    private int quantity;
    @ManyToOne(cascade = CascadeType.ALL)
    private Equity equity;
    private String symbol;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cash cash;
    @ManyToOne(cascade = CascadeType.ALL)
    private Margin margin;

    protected EquityPosition() {
        super();
    }

    public EquityPosition(int quantity, String symbol) {
        this();
        this.quantity = quantity;
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Equity getEquity() {
        return equity;
    }

    public void setEquity(Equity equity) {
        this.equity = equity;
    }

    public void buy(int amount) {
        this.quantity += amount;
    }

    public void sell(int amount) {
        this.quantity -= amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Cash getCash() {
        return cash;
    }

    public void setCash(Cash cash) {
        this.cash = cash;
    }

    public Margin getMargin() {
        return margin;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
    }
}
