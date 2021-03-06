package com.bitforcestudio.usermanager.mapper;

import com.bitforcestudio.usermanager.model.entity.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    public boolean createUserTable();

    public int createNewUser(User user);

    public boolean removeUser(Integer userId);

    public int updateUser(User user);

    public User getUserById(@Param("userId") Integer userId);

    public User getUserByUserName(@Param("userName") String userName);
}