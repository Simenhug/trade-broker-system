package com.simen.tradesystem.position;

import com.simen.tradesystem.account.Cash;
import com.simen.tradesystem.account.Margin;
import com.simen.tradesystem.core.BaseEntity;
import com.simen.tradesystem.securities.Options;
import quote.Quote;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class OptionPosition extends BaseEntity {

    private int quantity;
    @ManyToOne
    private Options options;
    private String symbol;

    @ManyToOne
    private Cash cash;
    @ManyToOne
    private Margin margin;

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

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public double marketValue() {
        return quantity* Quote.getOptionLastPrice(options.getSymbol());
    };

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
