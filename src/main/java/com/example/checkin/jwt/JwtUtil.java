package com.example.checkin.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String secret = String.valueOf(Jwts.SIG.HS256.key().build());
    private static final Key KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    public String generateToken(UserDetails userDetails){
        Map<String , Object> claims = new HashMap<>();
        claims.put("role" , userDetails.getAuthorities().iterator().next().getAuthority());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(KEY)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith((SecretKey) KEY). build().parseSignedClaims(token).getPayload();
    }

    public String extractusername(String token){
        return getClaims(token).getSubject();
    }

    public Boolean ValidateToken(String token , UserDetails userDetails){
        return extractusername(token).equals(userDetails.getUsername());
    }
}
