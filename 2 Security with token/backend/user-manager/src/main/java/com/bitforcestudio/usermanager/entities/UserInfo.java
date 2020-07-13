package com.bitforcestudio.usermanager.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfo {
    private String username;
    private String modifiedTime;
    private String role;
}