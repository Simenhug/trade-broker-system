package com.simen.tradesystem.securities;

import com.simen.tradesystem.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Option extends BaseEntity {
    @NotNull
    private String symbol;
    private Long price;
    @ManyToOne
    private Equity UNDERLYING;
    private boolean ITM;

    protected Option() {
        super();
    }
    public Option(String symbol, Equity underlying) {
        this();
        this.symbol = symbol;
        setUNDERLYING(underlying);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Equity getUNDERLYING() {
        return UNDERLYING;
    }

    public void setUNDERLYING(Equity UNDERLYING) {
        this.UNDERLYING = UNDERLYING;
        UNDERLYING.addOptions(this);
    }

    public boolean isITM() {
        return ITM;
    }

    public void setITM(boolean ITM) {
        this.ITM = ITM;
    }
}
