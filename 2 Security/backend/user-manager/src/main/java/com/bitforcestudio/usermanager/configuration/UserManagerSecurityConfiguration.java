package com.bitforcestudio.usermanager.configuration;

import com.bitforcestudio.usermanager.security.AuthenticationEntry;
import com.bitforcestudio.usermanager.security.AuthenticationProvider;
import com.bitforcestudio.usermanager.security.AuthenticationTokenFilter;
import com.bitforcestudio.usermanager.security.handler.AuthenticationFailureHandlerImpl;
import com.bitforcestudio.usermanager.security.handler.AuthenticationSuccessHandlerImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class UserManagerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntry unauthorizedHandler;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManager());
        authenticationTokenFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerImpl());
        return authenticationTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .authorizeRequests().antMatchers("/api/**").authenticated()
                .and()
                // Call our errorHandler if authentication/authorisation fails
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                .and()
                // don't create session (REST)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();

        httpSecurity
            .formLogin()
            .loginProcessingUrl("/loginForm")
            .successHandler(new AuthenticationSuccessHandlerImpl())
            .failureHandler(new AuthenticationFailureHandlerImpl());
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        //Default users to grant access
        authenticationManagerBuilder
            .inMemoryAuthentication()
            .withUser("user").password("test123").authorities("USER").and()
            .withUser("admin").password("test123").authorities("ADMIN");

        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    }
}