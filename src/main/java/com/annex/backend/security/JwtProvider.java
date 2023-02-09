package com.annex.backend.security;

import com.annex.backend.config.RsaKeyProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

@Service
@AllArgsConstructor
@Data
@Lazy
public class JwtProvider {

    private final RsaKeyProperties rsaKeyProperties;

    private static final int jwtExpirationMillis = 900000;

    public String generateToken(Authentication authentication){
        System.out.println("generating");
        return Jwts
                .builder().setSubject(authentication.getName())
                .signWith(rsaKeyProperties.privateKey())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationMillis)))
                .compact();
    }

    public String generateTokenWithMail(String mail){
        return Jwts
                .builder().setSubject(mail).signWith(rsaKeyProperties.privateKey())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationMillis)))
                .compact();
    }

    @Bean
    public String getEmailFromJwt(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(rsaKeyProperties.publicKey()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
