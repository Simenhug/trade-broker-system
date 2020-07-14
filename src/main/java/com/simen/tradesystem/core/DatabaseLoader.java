package com.simen.tradesystem.core;

import com.simen.tradesystem.account.*;
import com.simen.tradesystem.securities.Equity;
import com.simen.tradesystem.securities.EquityRepository;
import com.simen.tradesystem.securities.Options;
import com.simen.tradesystem.securities.OptionRepository;
import com.simen.tradesystem.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

//only needs to run once.
//@Component
public class DatabaseLoader implements ApplicationRunner {
    private final EquityRepository equities;
    private final OptionRepository options;
    private final CashRepository cash;
    private final MarginRepository margin;
    private final MarginService marginService;
    private final CashService cashService;
    private final UserRepository users;
    private final DetailsService userService;
    private final RoleRepository roleRepository;


    @Autowired
    public DatabaseLoader(EquityRepository equities, OptionRepository options, CashRepository cash, MarginRepository margin, MarginService marginService, CashService cashService, UserRepository users, DetailsService userService, RoleRepository roleRepository) {
        this.equities = equities;
        this.options = options;
        this.cash = cash;
        this.margin = margin;
        this.marginService = marginService;
        this.cashService = cashService;
        this.users = users;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Equity> stockPool = Arrays.asList(
                new Equity("PDD", 0.25),
                new Equity("GE", 0.5),
                new Equity("SPY", 0.75),
                new Equity("GOOG", 0.25),
                new Equity("FB", 0.25),
                new Equity("AAPL", 0.25),
                new Equity("AMZN", 0.25),
                new Equity("NFLX", 0.25),
                new Equity("MSFT", 0.25),
                new Equity("INTC", 0.25),
                new Equity("TSLA", 0.75),
                new Equity("UGAZ", 1.0)
        );
        equities.saveAll(stockPool);
        List<Options> optionsPool = Arrays.asList(
                new Options("SPY200619P0033000", equities.findBySymbol("SPY")),
                new Options("SPY200808P0031600", equities.findBySymbol("SPY")),
                new Options("AAPL200619C0034000", equities.findBySymbol("AAPL")),
                new Options("AAPL200918P0034500", equities.findBySymbol("AAPL")),
                new Options("MSFT200717C0017500", equities.findBySymbol("MSFT")),
                new Options("MSFT200717C0020000", equities.findBySymbol("MSFT")),
                new Options("SPY200821C0026600", equities.findBySymbol("SPY")),
                new Options("SPY200821P0026600", equities.findBySymbol("SPY")),
                new Options("SPY200706C0028300", equities.findBySymbol("SPY"))
        );
        options.saveAll(optionsPool);
        roleRepository.save(new Role("ROLE_USER"));
        roleRepository.save(new Role("ROLE_ADMIN"));
        Role role_user = roleRepository.findByName("ROLE_USER");
        Role role_admin = roleRepository.findByName("ROLE_ADMIN");
        List<User> betaUsers = Arrays.asList(
                new User("della", "della",  "huang", "password", role_user),
                new User("alita", "alita",  "battleangel", "password", role_user),
                new User("simen", "Huang", "simen", "123456", role_admin)
        );
        users.saveAll(betaUsers);
        Cash simen = new Cash("simen", userService.findByUsername("simen"));
        Cash della = new Cash("della", userService.findByUsername("della"));
        Margin alita = new Margin("alita", userService.findByUsername("alita"));
//        Cash simen = new Cash("simen", new User("simen", "simen", "huang", "123456", new Role("ROLE_ADMIN")));
//        Cash della = new Cash("della", new User("della", "della",  "huang", "password", new Role ("ROLE_USER")));
//        Margin alita = new Margin("alita", new User("alita", "alita",  "battleangel", "password", new Role ("ROLE_USER")));
        cash.save(simen);
        cash.save(della);
        margin.save(alita);
        cashService.deposit(10000000, simen);
        cashService.deposit(10000000, della);
        cashService.buyStock("GE", 1000, simen);
        cashService.buyStock("GE", 1000, della);
        cashService.buyStock("SPY", 1000, simen);
        cashService.sellStock("GE", 400, simen);
        cashService.sellStock("GE", 700, della);
        cashService.buyStock("UGAZ", 899, della);
        cashService.buyStock("UGAZ", 1000, simen);
        cashService.buyStock("GOOG", 1000, simen);
        cashService.sellStock("UGAZ", 1000, simen);
        cashService.buyStock("GE", 600, simen);
        cashService.buyOption("SPY200821C0026600", 10, simen);
        cashService.buyOption("SPY200821C0026600", 25, simen);
        cashService.buyOption("SPY200821C0026600", 35, della);
        cashService.buyOption("MSFT200717C0020000", 6, della);
        cashService.sellOption("SPY200821C0026600", 35, della);
        cashService.sellOption("SPY200821C0026600", 15, simen);
        cashService.buyOption("MSFT200717C0017500", 5, simen);
        marginService.deposit(10000000, alita);
        marginService.buyStock("TSLA", 2000, alita);
        marginService.buyStock("AMZN", 1000, alita);
        marginService.buyOption("AAPL200918P0034500", 10, alita);
        marginService.buyOption("MSFT200717C0017500", 5, alita);
        marginService.buyOption("AAPL200619C0034000", 15, alita);
        marginService.sellOption("AAPL200619C0034000", 15, alita);
        marginService.sellOption("AAPL200918P0034500", 7, alita);
    }
}
