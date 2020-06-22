package com.bitforcestudio.usermanager.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import com.bitforcestudio.usermanager.security.dto.UserDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {
    
    @Value("${jwt.secret}")
    private String secret;

    public UserDto parseToken(String token) {
        UserDto user = null;

        try {
            Claims body = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();

            user = new UserDto();
            user.setUsername(body.getSubject());
            user.setRole((String) body.get("role"));
        } catch (ExpiredJwtException  e) {
            e.printStackTrace();
        } catch (JwtException e) {
            e.printStackTrace();
        }

        return user;
    }
}