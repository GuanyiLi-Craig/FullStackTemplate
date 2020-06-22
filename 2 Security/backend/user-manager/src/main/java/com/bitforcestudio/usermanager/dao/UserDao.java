package com.bitforcestudio.usermanager.dao;

import com.bitforcestudio.usermanager.entities.UserEntity;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    public boolean createUserTable();

    public int createNewUser(UserEntity user);

    public boolean removeUser(Integer userId);

    public int updateUser(UserEntity user);

    public UserEntity getUserById(@Param("userId") Integer userId);

    public UserEntity getUserByUserName(@Param("userName") String userName);
}