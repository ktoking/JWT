# springboot-jwt-2020

### JWT原理
服务器认证以后会生产一个json对象，服务器完全只靠这个json对象校验用户身份，
为了防止json串被篡改，服务器在生成这个json对象时会进行签名

也就是说服务器端不保存这个数据，每次客户端请求时需要带着这个json对象

#### JWT数据结构
形如 xxxx.yyy.zzz 由三部分组成，每部分用英文句号连接

JWT的三个部分：
header 头部
payload 负载
signature 签名

也就是 Header.Payload.Signature

##### 1、Header 头部
是一个JSON 对象, 描述JWT的元数据，形如：
{"alg": "HS256", "typ": "JWT"}

alg属性表示签名的算法（algorithm）,默认是 HMAC SHA256

typ属性表示这个令牌的类型（type）,JWT 令牌统一写为JWT

##### 2、payload 负载
是一个JSON 对象, 用来存放实际需要传递的数据，形如：
{"sub": "1234567890", "name": "John Doe","admin": true}

一般是在这个部分定义私有字段：
例如{"userId":"1","userName":"jack"}

其中payload官方规定了7个字段：

iss (issuer)：签发人

exp (expiration time)：过期时间

sub (subject)：主题

aud (audience)：受众

nbf (Not Before)：生效时间

iat (Issued At)：签发时间

jti (JWT ID)：编号

注意，JWT 默认是不加密的，任何人都可以读到，所以不要把机密信息放在这个部分。

##### 3、signature 签名
signature 是对前两部分的签名，防止数据篡改

1、需要指定一个密钥（secret）
2、这个密钥只有服务器才知道，不能泄露给客户端
3、使用 Header 里面指定的签名算法，按照下面的公式产生签名。
    
    `HMACSHA256(
       base64UrlEncode(header) + "." +
       base64UrlEncode(payload),
       secret
     )`

也就是signature等于上面公式算出来的

把 Header、Payload、Signature 三个部分拼成一个字符串: xxxx.yyy.zzz

其中base64UrlEncode是串型化算法，处理特殊字符，=被省略、+替换成-，/替换成_

#### JWT 使用方式
客户端收到服务器返回的 JWT，可以储存在 Cookie 里面，也可以储存在 localStorage
以后客户端每次与服务器通信，都要带上这个 JWT

方式1、可以放在 Cookie 里面自动发送，但是这样不能跨域

方式2、更好的做法是放在 HTTP 请求的头信息Authorization字段里面

    Authorization: Bearer <token>
    
方式3、JWT放在POST请求的数据体body里面

#### JWT 的几个特点
（1）JWT 默认是不加密，但也是可以加密的。生成原始 Token 以后，可以用密钥再加密一次。

（2）JWT 不加密的情况下，不能将秘密数据写入 JWT。

（3）JWT 不仅可以用于认证，也可以用于交换信息。有效使用 JWT，可以降低服务器查询数据库的次数。

（4）JWT 的最大缺点是，由于服务器不保存 session 状态，因此无法在使用过程中废止某个 token，或者更改 token 的权限。也就是说，一旦 JWT 签发了，在到期之前就会始终有效，除非服务器部署额外的逻辑。

（5）JWT 本身包含了认证信息，一旦泄露，任何人都可以获得该令牌的所有权限。为了减少盗用，JWT 的有效期应该设置得比较短。对于一些比较重要的权限，使用时应该再次对用户进行认证。

（6）为了减少盗用，JWT 不应该使用 HTTP 协议明码传输，要使用 HTTPS 协议传输。




以上来自http://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html
有删减