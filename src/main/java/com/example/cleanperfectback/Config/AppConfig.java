package com.example.cleanperfectback.Config;

import com.example.cleanperfectback.Utils.NumberUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public NumberUtils numberUtils() {
        return new NumberUtils();
    }
}