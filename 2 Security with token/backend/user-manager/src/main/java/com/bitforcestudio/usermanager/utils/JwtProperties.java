package com.bitforcestudio.usermanager.utils;

public class JwtProperties {
    public static final String SECRET = "some_arbitrary_name";
    public static final int EXPIRATION_TIME = 86400000; // 1 day in millisecond
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
}