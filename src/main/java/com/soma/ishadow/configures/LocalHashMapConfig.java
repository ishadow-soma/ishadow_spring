package com.soma.ishadow.configures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Configuration
//@Profile("local")
public class LocalHashMapConfig {

    @Bean
    public Set<String> AuthenticationRepository() {
        return new HashSet<>();
    }

    @Bean
    public Set<String> URLRepository() {
        return new HashSet<>();
    }
}
