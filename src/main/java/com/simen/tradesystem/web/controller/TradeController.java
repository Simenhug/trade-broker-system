package com.simen.tradesystem.web.controller;

import com.simen.tradesystem.account.CashService;
import com.simen.tradesystem.account.MarginService;
import com.simen.tradesystem.position.EquityPositionService;
import com.simen.tradesystem.position.OptionPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TradeController {
    @Autowired
    private EquityPositionService equityPositionService;

    @Autowired
    private OptionPositionService optionPositionService;

    @Autowired
    private MarginService marginService;

    @Autowired
    private CashService cashService;

    @RequestMapping("/")
    public String homePage(Model model) {
        return "sample";
    }
}
