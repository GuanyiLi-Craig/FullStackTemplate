package com.bitforcestudio.usermanager.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 31774009846893656L;

    private Integer userId;
    private String userName;
    private String password;
    private Integer userStats;
    private Boolean credentialExpired;
    private Boolean isEnabled;
    private String roles;
    private String permissions;
    private Date modifiedTime;

    public User(Integer userId, String userName, String password) {
        this(userId, userName, password, 0, false, true, "USER", "PROJECT_CREATE,PROJECT_DELETE,PROJECT_MODIFY,USER_MODIFY,USER_DEACTIVE", new Date(System.currentTimeMillis()));
    }

    public User(String userName, String password) {
        this(-1, userName, password, 0, false, true, "USER", "PROJECT_CREATE,PROJECT_DELETE,PROJECT_MODIFY,USER_MODIFY,USER_DEACTIVE", new Date(System.currentTimeMillis()));
    }

    public List<String> getRolesList() {
        if (roles.length() > 0) {
            return Arrays.asList(roles.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getPermissionsList() {
        if (permissions.length() > 0) {
            return Arrays.asList(permissions.split(","));
        }
        return new ArrayList<>();
    }
}