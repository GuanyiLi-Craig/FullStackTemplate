package com.bitforcestudio.usermanager.configuration;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitforcestudio.usermanager.service.impl.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class UserManagerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/login**").permitAll()
            .antMatchers("/signup**").permitAll()
            .antMatchers("/home").permitAll()
            .antMatchers("/user/**").authenticated()
            .antMatchers("/admin/**").hasRole("ADMIN").and()
            .formLogin().loginPage("/login")
                        .successHandler(new AuthSuccessHandler())
                        .failureHandler(new AuthFailHandler())
                        .loginProcessingUrl("/login")
                        .and()
                        .logout().logoutSuccessHandler(new LogoutSuccessHandler())
                        .logoutUrl("/logout")
            .and()
            .httpBasic().disable()
            .csrf().disable()
            .cors();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);

        return authenticationProvider;
    }

    // define after login handler
    // success handler
    private class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            
            log.info("user [" + SecurityContextHolder.getContext().getAuthentication().getPrincipal() +"] login successful");
            // remove session code after login successfully
            request.getSession().removeAttribute("codeValue");
            request.getSession().removeAttribute("codeTime");
            
            PrintWriter out = response.getWriter();
            out.write("{\"status\":\"ok\",\"msg\":\"login success\"}");
            out.flush();
            out.close();
        }
    }

    // failed handler
    private class AuthFailHandler extends SimpleUrlAuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            //remove session code after login failed
            request.getSession().removeAttribute("codeValue");
            request.getSession().removeAttribute("codeTime");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter out = response.getWriter();
            out.write("{\"status\":\"error\",\"msg\":\"login failed\"}");
            out.flush();
            out.close();
        }
    }

    // customize exceptions
    public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.sendError(HttpStatus.UNAUTHORIZED.value(),authException.getMessage());
        }

    }
    
    // define log out message
    private class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler  {

        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                Authentication authentication) throws IOException, ServletException {
            PrintWriter out = response.getWriter();
            out.write("{\"status\":\"ok\",\"msg\":\"logout success\"}");
            out.flush();
            out.close();
        }
    }
}