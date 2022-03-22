package com.example.project.service;

import com.example.project.dto.AppUserDto;
import com.example.project.dto.RoleDto;
import com.example.project.entity.AppUser;
import com.example.project.entity.Role;

import java.util.List;

public interface UserService {
    AppUserDto saveUser(AppUserDto user);
    RoleDto saveRole(RoleDto role);
    void addRoleToUser(String username, String roleName);
    AppUserDto getUser(String username);
    List<AppUserDto> getUsers();

}
