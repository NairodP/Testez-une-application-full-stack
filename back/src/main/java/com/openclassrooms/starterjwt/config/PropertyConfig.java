package com.openclassrooms.starterjwt.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PropertyConfig {

    private final Dotenv dotenv;

    public PropertyConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(ConfigurableEnvironment environment) {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        
        try {
            Dotenv dotenv = Dotenv.configure()
                .directory("/Users/dorianpernot/Desktop/Testez-une-application-full-stack/back")
                .ignoreIfMissing()
                .load();
            
            Map<String, Object> properties = new HashMap<>();
            properties.put("DB_URL", dotenv.get("DB_URL"));
            properties.put("DB_USERNAME", dotenv.get("DB_USERNAME"));
            properties.put("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
            
            MapPropertySource propertySource = new MapPropertySource("dotenvProperties", properties);
            environment.getPropertySources().addFirst(propertySource);
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier .env: " + e.getMessage());
        }
        
        return configurer;
    }
}