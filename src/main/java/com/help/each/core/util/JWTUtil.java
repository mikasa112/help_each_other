package com.help.each.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.help.each.config.AppConfig;
import com.help.each.core.constant.Consts;
import com.help.each.core.constant.Status;
import com.help.each.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.help.each.core.exception.SecurityException;

/**
 * @author Yuanan
 * @date 2023/3/25
 * @description JWT工具类
 */
@Component
@AllArgsConstructor
public class JWTUtil {
    //秘钥
    private static final String SECRET_KEY = "214125442A472D4B6150645367566B59703373367639792F423F4528482B4D62";
    private final AppConfig appConfig;
    private final RedisUtil redisUtil;

    /**
     * 解析JWT，若有错抛出异常
     *
     * @param token jwt token
     * @return {@link Claims}
     */
    public Claims parseJWT(String token) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            String redisKey = Consts.REDIS_JWT_KEY_PREFIX + username;
            //从redis中取得username对应的token
            String v = redisUtil.getObject(redisKey);
            //如果token不存在
            if (ObjectUtil.isNull(v)) {
                throw new SecurityException(Status.TOKEN_EXPIRED);
                //如果token不一致，说明登录的地方不一样，建议重新登录
            } else if (!StrUtil.equals(token, v)) {
                throw new SecurityException(Status.TOKEN_OUT_OF_CTRL);
            }
            return claims;
        } catch (ExpiredJwtException e) {
            throw new SecurityException(Status.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException
                 | MalformedJwtException
                 | io.jsonwebtoken.security.SecurityException
                 | IllegalArgumentException e) {
            throw new SecurityException(Status.TOKEN_PARSE_ERROR);
        }
    }

    private @NotNull Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, @NotNull Function<Claims, T> claimsResolver) {
        final Claims claims = parseJWT(token);
        return claimsResolver.apply(claims);
    }

    /**
     * @param token jwt token
     * @return 用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, @NotNull UserDetails userDetails) {
        // 从token中获取用户名
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 生成token
     *
     * @param user        {@link User}
     * @param userDetails {@link UserDetails}
     * @param rememberMe  记住我
     * @return token
     */
    public String generateToken(User user, UserDetails userDetails, Boolean rememberMe) {
        return createJwt(user.getUuid(), user.getUsername(), rememberMe, userDetails.getAuthorities());
    }

    /**
     * 根据对应信息创建jwt
     *
     * @param uuid        uuid
     * @param username    用户名
     * @param rememberMe  记住我
     * @param authorities 权限列表
     * @return token
     */
    public String createJwt(String uuid, String username, Boolean rememberMe, Collection<? extends GrantedAuthority> authorities) {
        Date now = new Date(System.currentTimeMillis());
        JwtBuilder builder = Jwts.builder()
                .setId(uuid)
                .setSubject(username)
                .setIssuedAt(now)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .claim("authorities", authorities);
        //如果没有记住我那就使用yaml中申明的ttl
        Long ttl = rememberMe ? appConfig.getJwt().getRemember() : appConfig.getJwt().getTtl();
        if (ttl > 0) {
            builder.setExpiration(DateUtil.offsetMillisecond(now, ttl.intValue()));
        }
        String token = builder.compact();
        //创建出来的jwt存入token
        redisUtil.setObject(Consts.REDIS_JWT_KEY_PREFIX + username, token, ttl.intValue(), TimeUnit.MILLISECONDS);
        return token;
    }

    /**
     * get jwt from http request
     *
     * @param request 请求
     * @return jwt token
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(appConfig.getJwt().getTokenHeader());
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 设置JWT过期
     *
     * @param request HttpServletRequest
     * @return success
     */
    public boolean invalidateJWT(HttpServletRequest request) {
        String jwt = getJwtFromRequest(request);
        String username = extractUsername(jwt);
        return redisUtil.deleteObject(Consts.REDIS_JWT_KEY_PREFIX + username);
    }

}
