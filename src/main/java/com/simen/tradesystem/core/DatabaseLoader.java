package com.simen.tradesystem.core;

import com.simen.tradesystem.account.*;
import com.simen.tradesystem.securities.Equity;
import com.simen.tradesystem.securities.EquityRepository;
import com.simen.tradesystem.securities.Option;
import com.simen.tradesystem.securities.OptionRepository;
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

    @Autowired
    public DatabaseLoader(EquityRepository equities, OptionRepository options, CashRepository cash, MarginRepository margin, MarginService marginService) {
        this.equities = equities;
        this.options = options;
        this.cash = cash;
        this.margin = margin;
        this.marginService = marginService;
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
        Cash c = new Cash("simen");
        c.deposit(10000000);
        c.buyStock("GE", 1000);
        c.buyStock("SPY", 1000);
        c.buyStock("UGAZ", 1000);
        c.buyStock("GOOG", 1000);
        c.buyOption("SPY200821C0026600", 10);
        Margin m = new Margin("alita");
        m.deposit(10000000);
        marginService.buyStock("TSLA", 2000, m);
        marginService.buyStock("AMZN", 1000, m);
        marginService.buyOption("AAPL200918P0034500", 10, m);
//        marginService.buyOption("MSFT200717C0017500", 5, m);
        cash.save(c);
        margin.save(m);
    }
}
