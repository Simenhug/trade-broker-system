package com.simen.tradesystem.core;

import com.simen.tradesystem.account.*;
import com.simen.tradesystem.position.EquityPositionRepository;
import com.simen.tradesystem.position.OptionPositionRepository;
import com.simen.tradesystem.securities.Equity;
import com.simen.tradesystem.securities.EquityRepository;
import com.simen.tradesystem.securities.Option;
import com.simen.tradesystem.securities.OptionRepository;
import com.simen.tradesystem.user.User;
import com.simen.tradesystem.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseLoader implements ApplicationRunner {
    private final EquityRepository equities;
    private final OptionRepository options;
    private final CashRepository cash;
    private final MarginRepository margin;
    private final MarginService marginService;
    private final CashService cashService;
    private final UserRepository users;


    @Autowired
    public DatabaseLoader(EquityRepository equities, OptionRepository options, CashRepository cash, MarginRepository margin, MarginService marginService, CashService cashService, UserRepository users) {
        this.equities = equities;
        this.options = options;
        this.cash = cash;
        this.margin = margin;
        this.marginService = marginService;
        this.cashService = cashService;
        this.users = users;
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
        List<Option>optionPool = Arrays.asList(
                new Option("SPY200619P0033000", equities.findBySymbol("SPY")),
                new Option("SPY200808P0031600", equities.findBySymbol("SPY")),
                new Option("AAPL200619C0034000", equities.findBySymbol("AAPL")),
                new Option("AAPL200918P0034500", equities.findBySymbol("AAPL")),
                new Option("MSFT200717C0017500", equities.findBySymbol("MSFT")),
                new Option("MSFT200717C0020000", equities.findBySymbol("MSFT")),
                new Option("SPY200821C0026600", equities.findBySymbol("SPY")),
                new Option("SPY200821P0026600", equities.findBySymbol("SPY")),
                new Option("SPY200706C0028300", equities.findBySymbol("SPY"))
        );
        options.saveAll(optionPool);
        List<User> betaUsers = Arrays.asList(
                new User("jacobproffer", "Jacob",  "Proffer", "password", new String[] {"ROLE_USER"}),
                new User("mlnorman", "Mike",  "Norman", "password", new String[] {"ROLE_USER"}),
                new User("k_freemansmith", "Karen",  "Freeman-Smith", "password", new String[] {"ROLE_USER"}),
                new User("seth_lk", "Seth",  "Kroger", "password", new String[] {"ROLE_USER"}),
                new User("mrstreetgrid", "Java",  "Vince", "password", new String[] {"ROLE_USER"}),
                new User("anthonymikhail", "Tony",  "Mikhail", "password", new String[] {"ROLE_USER"}),
                new User("boog690", "AJ",  "Teacher", "password", new String[] {"ROLE_USER"}),
                new User("faelor", "Erik",  "Faelor Shafer", "password", new String[] {"ROLE_USER"}),
                new User("christophernowack", "Christopher",  "Nowack", "password", new String[] {"ROLE_USER"}),
                new User("calebkleveter", "Caleb",  "Kleveter", "password", new String[] {"ROLE_USER"}),
                new User("richdonellan", "Rich",  "Donnellan", "password", new String[] {"ROLE_USER"}),
                new User("albertqerimi", "Albert",  "Qerimi", "password", new String[] {"ROLE_USER"})
        );
        users.saveAll(betaUsers);
        users.save(new User("simen", "Huang", "simen", "123456", new String[]{"ROLE_USER", "ROLE_ADMIN"}));
        Cash c = new Cash("simen");
        Cash ca = new Cash("della");
        Margin m = new Margin("alita");
        cash.save(c);
        margin.save(m);
        cashService.deposit(10000000, c);
        cashService.deposit(10000000, ca);
        cashService.buyStock("GE", 1000, c);
        cashService.buyStock("GE", 1000, ca);
        cashService.buyStock("SPY", 1000, c);
        cashService.sellStock("GE", 400, c);
        cashService.sellStock("GE", 700, ca);
        cashService.buyStock("UGAZ", 899, ca);
        cashService.buyStock("UGAZ", 1000, c);
        cashService.buyStock("GOOG", 1000, c);
        cashService.sellStock("UGAZ", 1000, c);
        cashService.buyStock("GE", 600, c);
        cashService.buyOption("SPY200821C0026600", 10, c);
        cashService.buyOption("SPY200821C0026600", 25, c);
        cashService.buyOption("SPY200821C0026600", 35, ca);
        cashService.buyOption("MSFT200717C0020000", 6, ca);
        cashService.sellOption("SPY200821C0026600", 35, ca);
        cashService.sellOption("SPY200821C0026600", 15, c);
        cashService.buyOption("MSFT200717C0017500", 5, c);
        marginService.deposit(10000000, m);
        marginService.buyStock("TSLA", 2000, m);
        marginService.buyStock("AMZN", 1000, m);
        marginService.buyOption("AAPL200918P0034500", 10, m);
        marginService.buyOption("MSFT200717C0017500", 5, m);
        marginService.buyOption("AAPL200619C0034000", 15, m);
        //TODO bugs while fully closing a position. creates concurrency exceptions
        marginService.sellOption("AAPL200619C0034000", 15, m);
        marginService.sellOption("AAPL200918P0034500", 7, m);
    }
}
