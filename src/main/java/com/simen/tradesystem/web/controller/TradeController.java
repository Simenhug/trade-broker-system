package com.simen.tradesystem.web.controller;

import com.simen.tradesystem.account.*;
import com.simen.tradesystem.position.*;
import com.simen.tradesystem.user.DetailsService;
import com.simen.tradesystem.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import quote.Quote;

import java.security.Principal;
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

    @RequestMapping("/")
    public String homePage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
//        System.out.println(user.getUsername() + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        List<EquityPosition> equities = new ArrayList<>();
        List<OptionPosition> options = new ArrayList<>();
        if (cashService.findByUser(user) != null) {
//            System.out.println("NOT NULL\n\n\n\n\n\n\n\n\n\n\n\n");
            Cash cash = cashService.findByUser(user);
            equities = equityPositionRepository.findCashPositions(cash.getId());
            options = optionPositionRepository.findCashPositions(cash.getId());
        } else {
            Margin margin = marginService.findByUser(user);
            equities = equityPositionRepository.findMarginPositions(margin.getId());
            options = optionPositionRepository.findMarginPositions(margin.getId());
        }
        model.addAttribute("equities", equities);
        model.addAttribute("options", options);
        return "account";
    }

    @RequestMapping("/quote")
    @ResponseBody
    public Double lastPrice(@RequestParam("symbol") String symbol) {
        if (symbol.length() <= 5) {
            return Quote.getStockLastPrice(symbol);
        } else {
            return Quote.getOptionLastPrice(symbol);
        }
    }
}
