package com.simen.position;

import com.simen.core.BaseEntity;
import com.simen.securities.Equity;
import quote.Quote;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class EquityPosition extends BaseEntity {

    private int quantity;
    @ManyToOne(cascade = CascadeType.ALL)
    private Equity equity;
    private String symbol;

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
}
