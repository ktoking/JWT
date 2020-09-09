package com.chilly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Chilly Cui on 2020/9/9.
 */
@RestController
public class HelloController {

    @RequestMapping("/test/test")
    public String test(String username, HttpServletRequest request) {

        //认证成功，放入session
        request.getSession().setAttribute("username",username);
        return "login ok";
    }

}
