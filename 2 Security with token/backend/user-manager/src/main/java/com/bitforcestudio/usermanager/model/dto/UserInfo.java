package com.bitforcestudio.usermanager.model.dto;

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