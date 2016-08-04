package com.monitise.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    public /*?!*/ int login(){

        return 17;
    }

    @RequestMapping(value = "/login" , method = RequestMethod.GET)
    public /*?!*/ int loginGet(){

        return 13;
    }
}
