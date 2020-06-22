package com.bitforcestudio.usermanager.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitforcestudio.usermanager.security.dto.UserDto;
import com.bitforcestudio.usermanager.security.util.TokenGenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        UserDto userDto = UserDto.buildFromAuthentication(authentication);
    
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().append(TokenGenerator.generateToken(userDto, jwtSecret)).flush();

    }
}