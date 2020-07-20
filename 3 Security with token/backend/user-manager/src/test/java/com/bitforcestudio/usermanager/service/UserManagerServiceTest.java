package com.bitforcestudio.usermanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.bitforcestudio.usermanager.mapper.UserMapper;
import com.bitforcestudio.usermanager.model.entity.User;
import com.bitforcestudio.usermanager.service.impl.UserManagerServiceImpl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class UserManagerServiceTest {

    private static UserManagerService userManagerService = new UserManagerServiceImpl();
    
    private static UserMapper userMapper = Mockito.mock(UserMapper.class);

    @BeforeAll
    public static void init() {
        ReflectionTestUtils.setField(userManagerService, "userMapper", userMapper);
    }
    
    @Test
    public void testSignup() {
        String mockUserName = "MockUser";
        String mockPassword = "MockPassword";

        Mockito.doReturn(1).when(userMapper).createNewUser(any());

        String result = userManagerService.signup(mockUserName, mockPassword);

        assertEquals("1", result);
    }

    @Test
    public void testGetUserByUserName() {
        String mockUserName = "MockUser";
        String mockPassword = "MockPassword";

        User user = new User(mockUserName, mockPassword);

        Mockito.doReturn(user).when(userMapper).getUserByUserName(mockUserName);

        User result = userManagerService.getUserbyUserName(mockUserName);

        assertEquals(0, result.toString().compareTo(user.toString()));
    }
}