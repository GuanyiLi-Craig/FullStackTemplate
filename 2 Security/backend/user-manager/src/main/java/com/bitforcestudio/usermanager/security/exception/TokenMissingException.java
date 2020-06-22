package com.bitforcestudio.usermanager.security.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenMissingException extends AuthenticationException {

    private static final long serialVersionUID = -7999919723736409867L;

    public TokenMissingException(String msg) {
        super(msg);
    }
}
