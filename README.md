

### 一. 什么是JWT
**JSON Web Token**

- JSON Web Token (JWT)是一个开放标准(RFC 7519)，它定义了一种紧凑的、自包含的方式，用于作为JSON对象在各方之间安全地传输信息。该信息可以被验证和信任，因为它是数字签名的。

#### 1.1 什么时候你应该用JSON Web Token
**下列场景中使用JSON Web Token是很有用的：**

- Authorization (授权) : 这是使用JWT的最常见场景。一旦用户登录，后续每个请求都将包含JWT，允许用户访问该令牌允许的路由、服务和资源。单点登录是现在广泛使用的JWT的一个特性，因为它的开销很小，并且可以轻松地跨域使用。

- Information Exchange (信息交换) : 对于安全的在各方之间传输信息而言，JSON Web Tokens无疑是一种很好的方式。因为JWT可以被签名，例如，用公钥/私钥对，你可以确定发送人就是它们所说的那个人。另外，由于签名是使用头和有效负载计算的，您还可以验证内容没有被篡改。

#### 1.2 认证流程
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201201162345993.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2tpbmd0b2s=,size_16,color_FFFFFF,t_70)
- **首先，前端通过Web表单将自己的用户名和密码发送到后端的接口。这一过程- -般是一 个HTTP POST请求。建议的方式是通过SSL加密的传输(https协议) ，从而避免敏感信息被嗅探。**

- **后端核对用户名和密码成功后，将用户的id等其他信息作为JWT Payload (负载)，将其与头部分别进行Base64编码拼接后签名，形成一个JWT(Token)。形成的JWT就是一个形同11. zzz. xxx的字符串。token head . payload . singurater**
- **后端将JWT字符串作为登录成功的返回结果返回给前端。 前端可以将返回的结果保存在localStorage或sessionStorage上， 退出登录时前端删除保存的JWT即可。**
- **前端在每次请求时将JWT放入HTTP Header中的Authorization位。 (解决XSS和XSRF问题)**
- **后端检查是否存在，如存在验证JWT的有效性。例如，检查签名是否正确;检查Token是否过期;检查Token的接收方是否是自己(可选)**
- **验证通过后后端使用JWT中包含的用户信息进行其他逻辑操作，返回相应结果。**

#### 1.3 JWT优势在哪?
- **简洁(Compact):可以通过URL，POST参 数或者在HTTP header发送，因为数据量小，传输速度也很快
自包含(Self-contained):负载中包含了所有用户所需要的信息，避免了多次查询数据库**

- **因为Token是 以JSON加密的形式保存在客户端的，所以JWT是跨语言的，原则上任何web形式都支持。**

- **不需要在服务端保存会话信息，特别适用于分布式微服务。**

#### 1.4 JWT具体包含信息
- **header**
标头通常由两部分组成:**令牌的类型**(即JWT) 和**所使用的签名算法**，例如HMAC SHA256或RSA。 它会使用Base64 编码组成JWT 结构的第一部分。
注意:Base64是一 种编码，也就是说，它是可以被翻译回原来的样子来的。它并不是一种加密过程。

```
{
	"alg" : "HS256"
	"typ" : "JWT"
}
```

- **Payload**
令牌的第二部分是有效负载，其中包含声明。**声明是有关实体(通常是用户)和其他数据的声明**。同样的，它会使用Base64 编码组成JWT结构的第二部分

```
{
	"sub" : "HS256"
	"name" : "wang"
	"admin" : "true"
}
```
- **Signature**
**前面两部分都是使用Base64进行编码的，即前端可以解开知道里面的信息。Signature 需要使用编码后的header和payload以及我们提供的一个密钥，然后使用header 中指定的签名算法(HS256) 进行签名。签名的作用是保证JWT没有被篡改过**

```
HMACSHA256 (base64Ur1Encode(header) + "." + base64Ur1Encode(payload) , secret); 
```

### 二. 实战演示
#### 2.1 整合pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.chilly</groupId>
    <artifactId>springboot-jwt-2020</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>springboot-jwt-2020</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--引入mybatis-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.3</version>
        </dependency>

        <!--引入jwt-->
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.10.3</version>
        </dependency>

        <!--引入mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.49</version>
        </dependency>

        <!--引入druid-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.23</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```

#### 2.2 测试JWT加密过程

```java
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
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDY4MTYxNzksInVzZXJJZCI6MjEsInVzZXJuYW1lIjoieGlhb2NoZW4ifQ.h1yaTia3FcTjULAc8Xlfa4M3yD-E7z47lcKc7mNw9CM";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!Q@W#E$R")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        System.out.println("用户Id：" + decodedJWT.getClaim("userId").asInt());
        System.out.println("用户名：" + decodedJWT.getClaim("username"));
        System.out.println("过期时间：" + decodedJWT.getExpiresAt());

//        用户Id：21
//        用户名：com.auth0.jwt.impl.JsonNodeClaim@1381794
//        过期时间：Tue Dec 01 17:49:39 CST 2020

    }


}

```
### 三. 封装工具类
**封装成工具类使用**
```java
package com.chilly.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * 2020 12.1
 * ktoking
 */
public class JWTUtils {
    private static String SECRET = "token!Q@W#E$R";

    /**
     * 生产token
     */
    public static String getToken(Map<String, String> map) {
        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7); //默认7天过期

        builder.withExpiresAt(instance.getTime());//指定令牌的过期时间
        String token = builder.sign(Algorithm.HMAC256(SECRET));//签名
        return token;
    }

    /**
     * 验证token
     */
    public static DecodedJWT verify(String token) {
        //如果有任何验证异常，此处都会抛出异常
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        return decodedJWT;
    }

//    /**
//     * 获取token中的 payload
//     */
//    public static DecodedJWT getToken(String token) {
//        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
//        return decodedJWT;
//    }
}

```
### 四. JWT 整合SpringBoot
**拦截器验证token**

**项目源代码 :** [项目git地址](https://github.com/ktoking/JWT) 