package com.bitforcestudio.usermanager.entities;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {
    private Integer userId;
    private String userName;
    private String password;
    private Integer userStats;
    private Date modifiedTime;

    public UserEntity(Integer userId, String userName, String password) {
        this(userId, userName, password, 0, new Date(System.currentTimeMillis()));
    }

    public UserEntity(String userName, String password) {
        this(-1, userName, password, 0, new Date(System.currentTimeMillis()));
    }
}