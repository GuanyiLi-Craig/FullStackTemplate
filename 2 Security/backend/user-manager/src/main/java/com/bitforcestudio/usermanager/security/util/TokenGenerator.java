package com.bitforcestudio.usermanager.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import com.bitforcestudio.usermanager.security.dto.UserDto;

public class TokenGenerator {
    public static String generateToken(UserDto u, String secret) {
        Claims claims = Jwts.claims().setSubject(u.getUsername());
        claims.put("role", u.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(DateTime.now().plusSeconds(30).toDate())
                .compact();
    }
}