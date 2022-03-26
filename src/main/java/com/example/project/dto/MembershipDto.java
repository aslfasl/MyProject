package com.example.project.dto;

import com.example.project.entity.ClientEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
    private ClientEntity client;

    public MembershipDto(LocalDate startDate, LocalDate endDate, boolean isActive) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }
}
