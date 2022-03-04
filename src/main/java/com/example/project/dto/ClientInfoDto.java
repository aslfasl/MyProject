package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfoDto {

    String phoneNumber;
    String address;
    LocalDate start;
    LocalDate expiration;
    ClientDto client;
}
