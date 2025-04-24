package com.openclassrooms.starterjwt.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    
    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .directory("/Users/dorianpernot/Desktop/Testez-une-application-full-stack/back") // Chemin modifié pour pointer vers le dossier back
                .ignoreIfMissing()
                .load();
    }
}