package com.simen.account;

import com.simen.core.BaseEntity;
import com.simen.position.EquityPosition;
import com.simen.position.OptionPosition;
import quote.Quote;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Margin extends BaseEntity {
    private String username;
    private double balance;
    @OneToMany(cascade = CascadeType.ALL)
    private List<EquityPosition> equities = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<OptionPosition> options = new ArrayList<>();

    protected Margin() {
        super();
    }

    public Margin(String username) {
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


}
