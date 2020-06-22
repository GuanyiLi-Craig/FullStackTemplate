package com.bitforcestudio.usermanager.controller;

import com.bitforcestudio.usermanager.entities.LoginEntity;
import com.bitforcestudio.usermanager.security.dto.UserDto;
import com.bitforcestudio.usermanager.security.util.TokenGenerator;
import com.bitforcestudio.usermanager.service.UserManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@Slf4j
public class UserManagerController {

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${server.port}")
    private String serverPort;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(value="/user/signup/{username}/{password}")
    public String signup(@PathVariable("username") String username, 
                        @PathVariable("password") String password) {
        return userManagerService.signup(username, password);
    }

    @GetMapping(value="/nuser/login/{username}/{password}")
    public Object nlogin(@PathVariable("username") String username, 
                        @PathVariable("password") String password) {
        return userManagerService.login(username, password);
    }

    @RequestMapping("/login")
    public ResponseEntity authlogin(@RequestBody LoginEntity loginEntity) {
        log.info("login entity " + loginEntity);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginEntity.getUsername(), passwordEncoder.encode(loginEntity.getPassword())));

        if (isAuthenticated(authentication)) {
          UserDto userDto = UserDto.buildFromAuthentication(authentication);
          return ResponseEntity.status(HttpStatus.CREATED).body(TokenGenerator.generateToken(userDto, jwtSecret));
        }
    
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    @GetMapping(value="/user/logout/{username}")
    public String logout(@PathVariable("username") String username) {
        return userManagerService.logout(username);
    }

    @GetMapping(value = "/user/init")
    public String initializeDatabase() {
        return serverPort + " ---> " + userManagerService.initialize(); 
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
      }
}