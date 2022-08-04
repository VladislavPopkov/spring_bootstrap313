package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.dao.UserDaoImpl;
import ru.kata.spring.boot_security.demo.model.User;

import java.security.Principal;

@Controller
public class HelloController {
    private final UserDaoImpl ud;

    public HelloController(UserDaoImpl ud) {
        this.ud = ud;
    }

    @GetMapping(value = "/user")
    public String printWelcome(ModelMap model, Principal principal) {
        User us = ud.findByName(principal.getName());
        model.addAttribute("messages", us);
        return "user";
    }
}
