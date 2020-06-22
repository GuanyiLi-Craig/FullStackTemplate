package com.bitforcestudio.usermanager.security;

import java.util.List;

import com.bitforcestudio.usermanager.security.authimpl.AuthenticatedUser;
import com.bitforcestudio.usermanager.security.authimpl.AuthenticationToken;
import com.bitforcestudio.usermanager.security.dto.UserDto;
import com.bitforcestudio.usermanager.security.exception.InvalidTokenException;
import com.bitforcestudio.usermanager.security.util.TokenValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private TokenValidator tokenValidator;

    @Override
    public boolean supports(Class<?> authentication) {
        return (AuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        
        AuthenticationToken authenticationToken = (AuthenticationToken) authentication;
        String token = authenticationToken.getToken();

        UserDto parsedUser = tokenValidator.parseToken(token);

        if (parsedUser == null) {
            throw new InvalidTokenException("JWT token is not valid");
        }

        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.getRole());
    
        return new AuthenticatedUser(parsedUser.getUsername(), token, authorityList);
    }
    
}