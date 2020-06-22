package com.bitforcestudio.usermanager.security;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 *  Entry point to filter unauthorized behavior
 * 
 */
@Component
public class AuthenticationEntry implements AuthenticationEntryPoint, Serializable {

	/**
     * auto generated series version id
     */
    private static final long serialVersionUID = -716879984148011916L;

    @Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Operation");
	}
}