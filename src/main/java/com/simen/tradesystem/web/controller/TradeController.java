package com.simen.tradesystem.web.controller;

import com.simen.tradesystem.account.*;
import com.simen.tradesystem.position.*;
import com.simen.tradesystem.user.DetailsService;
import com.simen.tradesystem.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import quote.Quote;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TradeController {
    @Autowired
    private EquityPositionService equityPositionService;
    @Autowired
    private OptionPositionService optionPositionService;
    @Autowired
    private EquityPositionRepository equityPositionRepository;
    @Autowired
    private OptionPositionRepository optionPositionRepository;
    @Autowired
    private MarginService marginService;
    @Autowired
    private MarginRepository marginRepository;
    @Autowired
    private CashService cashService;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private DetailsService userService;

    //TODO support LIMIT, STOP, STOPLIMIT, TRAILING STOP order types

    @RequestMapping("/")
    public String homePage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
//        System.out.println(user.getUsername() + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        List<EquityPosition> equities = new ArrayList<>();
        List<OptionPosition> options = new ArrayList<>();
        double balance;
        double mv;
        double net_equity;
        if (cashService.findByUser(user) != null) {
//            System.out.println("NOT NULL\n\n\n\n\n\n\n\n\n\n\n\n");
            Cash cash = cashService.findByUser(user);
            equities = equityPositionRepository.findCashPositions(cash.getId());
            options = optionPositionRepository.findCashPositions(cash.getId());
            balance = Double.valueOf(new DecimalFormat("#.##").format(cash.getBalance()));
            mv = cashService.getTotalMarketValue(cash);
            net_equity = cashService.getNetWorth(cash);
        } else {
            Margin margin = marginService.findByUser(user);
            equities = equityPositionRepository.findMarginPositions(margin.getId());
            options = optionPositionRepository.findMarginPositions(margin.getId());
            balance = Double.valueOf(new DecimalFormat("#.##").format(margin.getBalance()));
            mv = marginService.getTotalMarketValue(margin);
            net_equity = marginService.getNetWorth(margin);
        }
        model.addAttribute("equities", equities);
        model.addAttribute("options", options);
        model.addAttribute("balance", balance);
        model.addAttribute("mv", mv);
        model.addAttribute("net_equity", net_equity);
        return "account";
    }

    @RequestMapping("/quote")
    @ResponseBody
    public Double lastPrice(@RequestParam("symbol") String symbol) {
        String query = symbol.replaceAll("[^A-Za-z0-9]+", "").toUpperCase();
        try {
            if (symbol.length() <= 5) {
                return Quote.getStockLastPrice(query);
            } else {
                return Quote.getOptionLastPrice(query);
            }
        } catch (IllegalArgumentException e) {
            return 0.00;
        }
    }

    // Search symbols to trade
    @RequestMapping("/search")
    public String formNewOrder(@RequestParam String q, Model model) {
        String query = q.replaceAll("[^A-Za-z0-9]+", "").toUpperCase();
        double price = 0.00;
        if (query.length() <= 5) {
            model.addAttribute("product", "equity");
            try {
                price = Quote.getStockLastPrice(query);
            } catch (IllegalArgumentException e) {
                price = 0.00;
            }
            model.addAttribute(price);
        } else {
            model.addAttribute("product", "option");
            try {
                price = Quote.getOptionLastPrice(query);
            } catch (IllegalArgumentException e) {
                price = 0.00;
            }
            model.addAttribute(price);
        }
        model.addAttribute("symbol", query);
        return "order";
    }

    // click on position to trade
    @RequestMapping("/orderForm/{symbol}")
    public String newOrderForm(@PathVariable String symbol, Model model) {
        String query = symbol.replaceAll("[^A-Za-z0-9]+", "").toUpperCase();
        double price = 0.00;
        if (query.length() <= 5) {
            model.addAttribute("product", "equity");
            try {
                price = Quote.getStockLastPrice(query);
            } catch (IllegalArgumentException e) {
                price = 0.00;
            }
            model.addAttribute(price);
        } else {
            model.addAttribute("product", "option");
            try {
                price = Quote.getOptionLastPrice(query);
            } catch (IllegalArgumentException e) {
                price = 0.00;
            }
            model.addAttribute(price);
        }
        model.addAttribute("symbol", query);
        return "order";
    }

    // equity orders
    // using Java Reflection
    @RequestMapping("/trade/equity/{symbol}")
    public String equityOrderExecution(@PathVariable String symbol, @RequestParam(name = "quantity")Integer quantity,
                                 @RequestParam(name = "side")String side, Principal principal) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user = userService.findByUsername(principal.getName());
        Object serviceType;
        Class accountType;
        Object account;
        if (cashService.findByUser(user) != null) {
//            System.out.println("NOT NULL\n\n\n\n\n\n\n\n\n\n\n\n");
            account = cashService.findByUser(user);
            serviceType = cashService;
            accountType = Cash.class;
        } else {
            account = marginService.findByUser(user);
            serviceType = marginService;
            accountType = Margin.class;
        }
        Class cls = serviceType.getClass();
        Method trade = cls.getDeclaredMethod(String.format("%sStock", side.toLowerCase()), String.class, Integer.class, accountType);
        trade.invoke(serviceType, symbol, quantity, account);
        return "redirect:/";
    }

    @RequestMapping("/trade/option/{symbol}")
    public String optionOrderExecution(@PathVariable String symbol, @RequestParam(name = "quantity")Integer quantity,
                                 @RequestParam(name = "side")String side, Principal principal) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user = userService.findByUsername(principal.getName());
        Object serviceType;
        Class accountType;
        Object account;
        if (cashService.findByUser(user) != null) {
//            System.out.println("NOT NULL\n\n\n\n\n\n\n\n\n\n\n\n");
            account = cashService.findByUser(user);
            serviceType = cashService;
            accountType = Cash.class;
        } else {
            account = marginService.findByUser(user);
            serviceType = marginService;
            accountType = Margin.class;
        }
        Class cls = serviceType.getClass();
        Method trade = cls.getDeclaredMethod(String.format("%sOption", side.toLowerCase()), String.class, Integer.class, accountType);
        trade.invoke(serviceType, symbol, quantity, account);
        return "redirect:/";
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    @ResponseBody
    public Double deposit(@RequestHeader("amount") Double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        System.out.println("ALL GOOD\n\n\n\n\n\n\n\n\n\n\n\n");
        double balance;
        if (cashService.findByUser(user) != null) {
            Cash cash = cashService.findByUser(user);
            cashService.deposit(amount, cash);
            balance = Double.valueOf(new DecimalFormat("#.##").format(cash.getBalance()));
        } else {
            Margin margin = marginService.findByUser(user);
            marginService.deposit(amount, margin);
            balance = Double.valueOf(new DecimalFormat("#.##").format(margin.getBalance()));
        }
        return balance;
    }
}
