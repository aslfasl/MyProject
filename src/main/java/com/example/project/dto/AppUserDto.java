package com.example.project.dto;

import com.example.project.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    private Long id;
    private String name;
    private String username;
    private String password;
    private List<RoleDto> roles = new ArrayList<>();
}
