package com.chilly;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class SpringbootJwt2020ApplicationTests {

    //令牌获取
    @Test
    void contextLoads() {
        Map<String, Object> map = new HashMap<>();

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 2000);

        String token = JWT.create().withHeader(map) //header
                .withClaim("userId", 21)//payload
                .withClaim("username", "xiaochen")//payload
                .withExpiresAt(instance.getTime())//指定令牌的过期时间
                .sign(Algorithm.HMAC256("!Q@W#E$R")) //签名
                ;
        System.out.println(token);
    }

    //令牌验证:根据令牌和签名解析数据
    //常见异常：
    //SignatureVerificationException 签名不一致异常
    //TokenExpiredException 令牌过期异常
    //AlgorithmMismatchException 算法不匹配异常
    //InvalidClaimException 失效的payload异常
    @Test
    void test() {
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTk2NDkxMzMsInVzZXJJZCI6MjEsInVzZXJuYW1lIjoieGlhb2NoZW4ifQ.LmTERviRHnmKpOeXO0f9K2nR1C7AovGfAV6Fmx7tcw0";
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTk2NTEzOTgsInVzZXJJZCI6MjEsInVzZXJuYW1lIjoieGlhb2NoZW4ifQ.KgJhjqs0T2WCijB9MDJQE9pCoKaC_eO3H6ILDmrhz1A";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!Q@W#E$R")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        System.out.println("用户Id：" + decodedJWT.getClaim("userId").asInt());
        System.out.println("用户名：" + decodedJWT.getClaim("username"));
        System.out.println("过期时间：" + decodedJWT.getExpiresAt());

//        用户Id：21
//        用户名：com.auth0.jwt.impl.JsonNodeClaim@236e3f4e
//        过期时间：Wed Sep 09 19:36:38 CST 2020

    }


}
