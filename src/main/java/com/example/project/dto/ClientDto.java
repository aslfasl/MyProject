package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String passport;
    private String address;
    private String phone;
    private LocalDate birthdate;
    private MembershipDto membership;
    private Collection<WorkoutClassDto> clientWorkouts;
}
