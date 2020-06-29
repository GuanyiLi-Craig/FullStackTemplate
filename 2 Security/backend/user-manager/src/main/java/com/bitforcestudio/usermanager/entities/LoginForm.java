package com.bitforcestudio.usermanager.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;

    public LoginForm (){}
}