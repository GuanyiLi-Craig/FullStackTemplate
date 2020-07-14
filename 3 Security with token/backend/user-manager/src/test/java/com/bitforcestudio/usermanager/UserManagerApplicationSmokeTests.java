package com.bitforcestudio.usermanager;

import static org.assertj.core.api.Assertions.assertThat;

import com.bitforcestudio.usermanager.mapper.UserMapper;
import com.bitforcestudio.usermanager.service.UserManagerService;
import com.bitforcestudio.usermanager.service.impl.UserDetailsServiceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class UserManagerApplicationSmokeTests {

    @MockBean
    UserMapper userMapper;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserManagerService userManagerService;

    @Test
    public void contextLoads() throws Exception {
        assertThat(userDetailsService).isNotNull();
        assertThat(userManagerService).isNotNull();
    }
    
}