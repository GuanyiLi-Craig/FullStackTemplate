package com.bitforcestudio.usermanager.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserBasic {
    private String username;
    private String password;
}