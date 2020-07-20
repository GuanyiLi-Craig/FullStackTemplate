package com.bitforcestudio.usermanager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bitforcestudio.usermanager.model.dto.UserBasic;
import com.bitforcestudio.usermanager.model.dto.UserInfo;
import com.bitforcestudio.usermanager.model.entity.User;
import com.bitforcestudio.usermanager.service.UserManagerService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

public class UserManagerControllerTest {

    private static UserManagerController userManagerController = new UserManagerController();

    private static UserManagerService userManagerService = Mockito.mock(UserManagerService.class);

    private static PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();


    @BeforeAll
    public static void init() {
        ReflectionTestUtils.setField(userManagerController, "userManagerService", userManagerService);
        ReflectionTestUtils.setField(userManagerController, "passwordEncoder", passwordEncoder);
    }

    @Test
    public void testSignupRequest() {
        String mockUserName = "MockUser";
        String mockPassword = "MockPassword";
        UserBasic userBasic = new UserBasic();

        userBasic.setUsername(mockUserName);
        userBasic.setPassword(mockPassword);

        Mockito.doReturn("1")
               .when(userManagerService)
               .signup(mockUserName, passwordEncoder.encode(userBasic.getPassword()));
        

        String result  = userManagerController.signup(userBasic);
        
        assertEquals(result, "1");
    }

    @Test
    public void testGetUserInfo() {
        String mockUserName = "MockUser";
        String mockPassword = "MockPassword";
        User user = new User(mockUserName, mockPassword);
        UserInfo userInfo = new UserInfo(user.getUserName(),
            user.getModifiedTime().toString(),
            user.getRoles().toString());
        Mockito.doReturn(user)
               .when(userManagerService)
               .getUserbyUserName(mockUserName);
        

        UserInfo result  = (UserInfo) userManagerController.getUserInfo(mockUserName);
        
        assertEquals(0 ,result.toString().compareTo(userInfo.toString()));
    }
}