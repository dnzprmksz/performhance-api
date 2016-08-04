package com.monitise.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http
                .authorizeRequests()
                    .antMatchers("/organizations/**").access("hasRole('MANAGER')")
                    .anyRequest().authenticated()

                .and()

                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/organizations",true)
                    .failureForwardUrl("/login?fail=true")

                .and()

                .logout()
                    .permitAll();
    }

    // temporary user db
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("123").roles("MANAGER");
    }
}