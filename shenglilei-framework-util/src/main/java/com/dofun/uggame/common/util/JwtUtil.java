/**
 * zhengwenbo
 */
package com.dofun.uggame.common.util;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

/**
 * JWT 接口鉴权工具类https://github.com/jwtk
 * https://jwt.io/
 */
@Slf4j
public final class JwtUtil {


    private JwtUtil() {
    }

    /**
     * 生成一个令牌
     *
     * @param issuer         签发者
     * @param subject        主题内容
     * @param base64Security 签名key
     * @param days           过期时间
     * @return token
     */
    public static String createJWT(String issuer, String subject, String base64Security, int days) {

        //设置允许校验时间误差3分钟
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusMinutes(3);
        Date now = com.dofun.uggame.common.util.DateUtils.date(localDateTime);
        // 添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
                //签发者
                .setIssuer(issuer)
                // jwt的签发时间
                .setIssuedAt(now)
                // 主题
                .setSubject(subject)
                // 设置签名
                .signWith(generalKey(base64Security), SignatureAlgorithm.HS256);

        // 添加Token过期时间
        if (days > 0) {
            // 系统时间之前的token都是不可以被承认的
            builder.setExpiration(DateUtils.addDays(now, days)).setNotBefore(now);
        }
        // 生成JWT
        return builder.compact();
    }

    /**
     * 根据过期时间，生产一个令牌
     *
     * @param issuer         主题内容
     * @param subject        主题内容
     * @param overdueDate    超时时间(yyyy-MM-dd HH:mm:ss)
     * @param base64Security 签名key
     * @return token
     */
    public static String createJWT(String issuer, String subject, String base64Security, Date overdueDate) {
        //设置允许校验时间误差3分钟
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusMinutes(3);
        Date now = com.dofun.uggame.common.util.DateUtils.date(localDateTime);
        // 添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
                //签发者
                .setIssuer(issuer)
                // jwt的签发时间
                .setIssuedAt(now)
                // 主题
                .setSubject(subject)
                // 设置签名
                .signWith(generalKey(base64Security), SignatureAlgorithm.HS256);

        // 添加Token过期时间
        if (overdueDate.compareTo(now) > 0) {
            // 是一个时间戳，代表这个JWT的过期时间；
            builder.setExpiration(overdueDate)
                    //是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
                    .setNotBefore(now);
        }
        // 生成JWT
        return builder.compact();
    }

    /**
     * 解析令牌
     *
     * @param issuer         签发者
     * @param jsonWebToken   令牌
     * @param base64Security 解密的签名key : base64编码的字符串
     * @return Claims
     */
    public static Claims parseJWT(String issuer, String jsonWebToken, String base64Security) {
        try {
            return Jwts.parserBuilder().requireIssuer(issuer).setSigningKey(generalKey(base64Security)).build().parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception e) {
            log.error("parseJWT error.", e);
            return null;
        }
    }

    /**
     * jwt过期时，重新生成新的 jwt
     *
     * @param token
     * @return token
     */
    public static String updateJWT(String issuer, String token, String base64Security, int days) {
        try {
            Claims claims = parseJWT(issuer, token, base64Security);
            if (claims != null) {
                return createJWT(issuer, claims.getSubject(), base64Security, days);
            }
        } catch (Exception e) {
            log.error("updateJWT error.", e);
        }
        return null;
    }

    /**
     * 解析令牌,负载Payload使用的Base64Url编码，解码也要用Base64Url
     *
     * @param token 令牌
     * @return
     */
    public static String getSubject(String token) {
        try {
            String[] strs = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(strs[1]), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(payload);
            return jsonObject.getString("sub");
        } catch (Exception e) {
            log.error("出错的token：{}", token, e);
            return null;
        }
    }

    /**
     * 生成密钥
     *
     * @param base64Security base64位编码的字符串
     * @return
     */
    private static Key generalKey(String base64Security) {
        //根据算法对key加密
        return Keys.hmacShaKeyFor(base64Security.getBytes(StandardCharsets.UTF_8));
    }
}
