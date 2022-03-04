package com.example.project.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;

public class Converter {

    @Setter
    private ObjectMapper objectMapper;

    public <T> T convertValue(Object fromValue, Class<T> toValueType){
        return objectMapper.convertValue(fromValue, toValueType);
    }
}
