package com.simen.securities;

import com.simen.core.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Equity extends BaseEntity{
    @NotNull
    private String symbol;
    @NotNull
    private double price;
    @OneToMany(mappedBy = "UNDERLYING", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Option> options;
    private double maintenanceRequirement;

    protected Equity() {
        super();
    }

    public Equity(String symbol, double maintenanceRequirement) {
        this();
        this.symbol = symbol;
        this.maintenanceRequirement = maintenanceRequirement;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void addOptions(Option option) {
        this.options.add(option);
    }

    public double getMaintenanceRequirement() {
        return maintenanceRequirement;
    }

    public void setMaintenanceRequirement(double maintenanceRequirement) {
        this.maintenanceRequirement = maintenanceRequirement;
    }
}




