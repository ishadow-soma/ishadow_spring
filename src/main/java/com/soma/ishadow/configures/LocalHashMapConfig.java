package com.soma.ishadow.configures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class LocalHashMapConfig {

    @Bean
    public Set<String> AuthenticationRepository() {
        return new HashSet<>();
    }

    @Bean
    public HashMap<String, Long> URLRepository() {
        return new HashMap<>();
    }

    @Bean
    public HashMap<Long, Date> convertorRepository() {
        return new HashMap<>();
    }

}
