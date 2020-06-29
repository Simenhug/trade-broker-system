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
    @OneToMany(mappedBy = "cash", cascade = CascadeType.ALL)
    private List<EquityPosition> equities;
    @OneToMany(mappedBy = "cash", cascade = CascadeType.ALL)
    private List<OptionPosition> options;

    protected Cash() {
        super();
    }

    public Cash(String username) {
        this();
        this.equities = new ArrayList<>();
        this.options = new ArrayList<>();
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
