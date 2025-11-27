package com.korit.security_study.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key KEY;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }


    public String generateAccessToken(String id) {
        return Jwts.builder()
                .subject("AccessToken") // 토큰의 용도를 설명하는 식별자
                .id(id) // 토큰에 고유한 식별자를 부여 (사용자 ID / Email) => 나중에 토큰 무효화나 사용자 조회할때 사용
                .expiration(new Date(new Date().getTime()+(1000L * 60L * 60L * 24L * 30L))) // 한달의 만료 기간 부여
                .signWith(KEY)
                .compact();
    }
    /*

    Claims : JWT의 Payload 영역, 즉 사용자 정보, 만료일자 등등의 정보가 담겨 있다.
    JwtException : 토큰이 잘못 되어 있을 경우(위변조, 만료) 발생하는 예외

    */
    public Claims getClaims(String token) throws JwtException {
        System.out.println("JwtUtils : getClaims");
        JwtParserBuilder jwtParserBuilder = Jwts.parser();
//        parsing을 위해 비밀키가 필요 -> 생성(로그인)될때 결정됨.

        jwtParserBuilder.setSigningKey(KEY);
        JwtParser jwtParser = jwtParserBuilder.build();
        return jwtParser.parseClaimsJws(token).getBody();       // 순수 Claims JWT를 파싱
    }

    public boolean isBearer(String token) {
        System.out.println("JwtUtils : isBearer");
        if(token == null) {
            return false;
        }
        if (!token.startsWith("Bearer ")) {
            return false;
        }
        return true;
    }

    public String removeBearer(String bearerToken) {
        System.out.println("JwtUtils : removeBearer");
        return bearerToken.substring(7).trim();
//        return bearerToken.replaceFirst("Bearer ", "");
    }



}
