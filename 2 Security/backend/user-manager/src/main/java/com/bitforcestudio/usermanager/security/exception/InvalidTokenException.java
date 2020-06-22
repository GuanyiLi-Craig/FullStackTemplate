package com.bitforcestudio.usermanager.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    private static final long serialVersionUID = -23652825913520835L;

    public InvalidTokenException(String msg) {
        super(msg);
    }
}
