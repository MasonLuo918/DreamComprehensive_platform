package com.Dream.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth")
public class OauthController {

    @RequestMapping("yiban")
    public String test(@RequestParam("user") String user, @RequestParam("userName") String userName){
        System.out.println(user+userName);
        return "hello";
    }
}
