package com.bitforcestudio.usermanager.security.dto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserDto {
    private String username;

    private String role;

    public static UserDto buildFromAuthentication(Authentication authentication) {
        UserDto userDto = new UserDto();
        userDto.setRole(authentication.getAuthorities().toArray()[0].toString());
        userDto.setUsername(((User) authentication.getPrincipal()).getUsername());
        return userDto;
    }
}