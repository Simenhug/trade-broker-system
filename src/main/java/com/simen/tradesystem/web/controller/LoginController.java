package com.simen.tradesystem.web.controller;

import com.simen.tradesystem.account.*;
import com.simen.tradesystem.user.User;
import com.simen.tradesystem.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    @Autowired
    private UserRepository users;

    @Autowired
    private CashRepository cashRepository;

    @Autowired
    private MarginRepository marginRepository;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
            // "flash" session attribute must not exist...do nothing and proceed normally
        }
        return "login";
    }

    @RequestMapping(path = "/signup", method = RequestMethod.GET)
    public String signUpForm(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        model.addAttribute("account", new Account());
        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
            // "flash" session attribute must not exist...do nothing and proceed normally
        }
        return "signup";
    }

    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public String createAccount(@ModelAttribute User user, @ModelAttribute Account account) {
        users.save(user);
        if (account.type.equals("margin")) {
            Margin margin = new Margin(user.getUsername(), user);
            marginRepository.save(margin);
        } else {
            Cash cash = new Cash(user.getUsername(), user);
            cashRepository.save(cash);
        }
        return "redirect:/";
    }

    @RequestMapping("/access_denied")
    public String accessDenied() {
        return "access_denied";
    }

    class Account{
        String type;

        public Account() {}

        //I can't have this constructor..... don't know why. If I add this, I'll get
        //java.lang.IllegalStateException: No primary or default constructor found for class but there is a default constructor
//        public Account(String type) {
//            this.type = type;
//        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
