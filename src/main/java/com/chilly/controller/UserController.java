package com.chilly.controller;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chilly.entity.User;
import com.chilly.service.UserService;
import com.chilly.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chilly Cui on 2020/9/9.
 */
@RestController
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/user/login")
    public Map<String, Object> login(User user) {
        log.info("用户名：{}", user.getName());
        log.info("password: {}", user.getPassword());

        Map<String, Object> map = new HashMap<>();

        try {
            User userDB = userService.login(user);

            Map<String, String> payload = new HashMap<>();
            payload.put("id", userDB.getId());
            payload.put("name", userDB.getName());
            String token = JWTUtils.getToken(payload);

            map.put("state", true);
            map.put("msg", "登录成功");
            map.put("token", token);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", e.getMessage());
            map.put("token", "");
        }
        return map;
    }

    @PostMapping("/user/test")
    public Map<String, Object> test(HttpServletRequest request) {
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtils.verify(token);
        String id = verify.getClaim("id").asString();
        String name = verify.getClaim("name").asString();
        log.info("用户id：{}", id);
        log.info("用户名: {}", name);

        //TODO 业务逻辑
        Map<String, Object> map = new HashMap<>();
        map.put("state", true);
        map.put("msg", "请求成功");
        return map;
    }

}
