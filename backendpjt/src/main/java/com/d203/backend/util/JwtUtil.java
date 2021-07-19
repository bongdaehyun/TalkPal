package com.d203.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    //암호화 변수
    private static final String secretKey="secret";
    //만료 시간
    private static final Long expireTime=1000L*6*360;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    //jwt 생성
    public String createToken(String email){
        Date now = new Date();
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(now.getTime()+expireTime))
                .sign(Algorithm.HMAC512(secretKey.getBytes()));
    }


    public static void main(String[] args) {
        JwtUtil jwt=new JwtUtil();
        String token=jwt.createToken("email@email.com");
        System.out.println("jwt = " + token);
    }
}
