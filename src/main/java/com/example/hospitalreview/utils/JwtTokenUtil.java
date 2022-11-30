package com.example.hospitalreview.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
// 토큰 발행
//jwt 를 디펜던시 해줘야함
public class JwtTokenUtil {
    public static String createToken(String userName, String key, long expireTimeMs) {
        Claims claims = Jwts.claims(); // 일종의 map 으로 key, value 로 값을 담을 수 있음
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 지금시간
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) // 지금시간 + 언제끝날지
                .signWith(SignatureAlgorithm.HS256, key) // 뭘로 사인할지? 받은 key로 잠금한다.
                .compact();

    }

}
