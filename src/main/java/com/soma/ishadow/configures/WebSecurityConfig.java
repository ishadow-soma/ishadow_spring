package com.soma.ishadow.configures;

import com.soma.ishadow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .headers().frameOptions().disable()
//                .and()
//                .authorizeRequests()
//                    .antMatchers(
//                        "/v2/api-docs",
//                        "/swagger-resources",
//                        "/swagger-resources/configuration/ui",
//                        "/swagger-resources/configuration/security")
//                    .permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.


    }
}


