package com.simen.tradesystem.account;

import com.simen.tradesystem.core.BaseEntity;
import com.simen.tradesystem.position.EquityPosition;
import com.simen.tradesystem.position.OptionPosition;
import com.simen.tradesystem.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Margin extends BaseEntity {
    private String username;
    private double balance;
    @OneToMany(mappedBy = "margin", cascade = CascadeType.ALL)
    private List<EquityPosition> equities;
    @OneToMany(mappedBy = "margin", cascade = CascadeType.ALL)
    private List<OptionPosition> options;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    protected Margin() {
        super();
    }

    public Margin(String username, User user) {
        this();
        this.equities = new ArrayList<>();
        this.options = new ArrayList<>();
        this.username = username;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
