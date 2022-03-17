package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClientEntityTest {

    @Autowired
    ObjectMapper objectMapper;


    // TODO: 16.03.2022 use this approach to reduce verbose in com.example.project.service.ClientServiceImp.updateClientById
    @SneakyThrows
    @Test
    void updateExample(){
        A a = new A();
        B b = new B();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        B b1 = objectMapper.updateValue(b, a);

        System.out.println("b1 = " + b1);
    }

    // TODO: 17.03.2022 testing git 2. Push in main?
    @Data
    class A{
        String present = "from A";
        String absent = null;
    }

    @Data
    class B{
        String onlyInB = "been always";
        String present = "B";
        String absent = "Still B";
    }


}