package com.sangeng.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@Slf4j
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("进入授权...");

        http
                .authorizeRequests()
                .antMatchers("/article/hotArticleList").permitAll()

                //.antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
                .cors();



    }
}
