package com.simen.position;

import com.simen.core.BaseEntity;
import com.simen.securities.Option;
import quote.Quote;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class OptionPosition extends BaseEntity {

    private int quantity;
    @ManyToOne
    private Option option;
    private String symbol;

    protected OptionPosition() {
        super();
    }

    public OptionPosition(int quantity, String symbol) {
        this();
        this.quantity = quantity;
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public void buy(int amount) {
        this.quantity += amount;
    }

    public void sell(int amount) {
        this.quantity -= amount;
    }

    public double marketValue() {
        return quantity* Quote.getOptionLastPrice(option.getSymbol());
    };

}
