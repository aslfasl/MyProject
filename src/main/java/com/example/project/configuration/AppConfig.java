package com.example.project.configuration;

import com.example.project.dto.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper mapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Bean
    public Converter creatorConverter(ObjectMapper objectMapper) {
        Converter converter = new Converter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
