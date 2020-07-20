package com.bitforcestudio.usermanager;

import static org.assertj.core.api.Assertions.assertThat;

import com.bitforcestudio.usermanager.mapper.UserMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class UserManagerApplicationRequestTests {
    @MockBean
    UserMapper userMapper;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void pong() throws Exception {
        assertThat (this.testRestTemplate.getForObject("http://localhost:" + 8002 + "/ping", 
                                                       String.class)).contains("pong");
    }
}