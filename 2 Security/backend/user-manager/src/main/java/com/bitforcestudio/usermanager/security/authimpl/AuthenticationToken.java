package com.bitforcestudio.usermanager.security.authimpl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;

@Getter
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = -2462071402974990121L;
    private String token;

    public AuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}