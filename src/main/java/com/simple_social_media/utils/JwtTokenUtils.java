package com.simple_social_media.utils;


import com.simple_social_media.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.lifetime}")
    private Duration lifetime;

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("roles", user.getRoles());

        Date issued = new Date();
        Date expired = new Date(issued.getTime() + lifetime.toMillis());

         return    Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getName())
                    .setExpiration(expired)
                    .setIssuedAt(issued)
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
    }

    public Claims getAllClaimsFromToken(String token ) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJwt(token)
                .getBody();
    }
    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }
    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    public Integer getId(String token) {
        return getAllClaimsFromToken(token).get("id", Integer.class);
    }
}
