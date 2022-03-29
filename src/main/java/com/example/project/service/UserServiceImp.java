package com.example.project.service;

import com.example.project.converter.Converter;
import com.example.project.dto.AppUserDto;
import com.example.project.dto.RoleDto;
import com.example.project.entity.AppUser;
import com.example.project.entity.Role;
import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.example.project.repo.RoleRepo;
import com.example.project.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.project.exception.ExceptionMessageUtils.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final Converter converter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getName()))
        );
        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public AppUserDto saveUser(AppUserDto userDto) {
        log.info("Saving new user {} to the database", userDto.getName());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userRepo.existsByUsername(userDto.getUsername())){
            throw new CustomException(USER_ALREADY_EXISTS, ErrorType.ALREADY_EXISTS);
        }
        AppUser appUser = userRepo.save(converter.convertAppUserDto(userDto));
        return converter.convertAppUser(appUser);
    }

    @Override
    public RoleDto saveRole(RoleDto roleDto) {
        log.info("Saving new role {} to the database", roleDto.getName());
        if (roleRepo.existsByName(roleDto.getName())){
            throw new CustomException(ROLE_ALREADY_EXISTS, ErrorType.ALREADY_EXISTS);
        }
        Role role = roleRepo.save(converter.convertValue(roleDto, Role.class));
        return converter.convertValue(role, RoleDto.class);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        AppUser user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        if (user == null) {
            throw new CustomException(USER_NOT_FOUND_NAME + username, ErrorType.NOT_FOUND);
        }
        if (role == null) {
            throw new CustomException(ROLE_NOT_FOUND_NAME + roleName, ErrorType.NOT_FOUND);
        }
        if (user.getRoles().contains(role)) {
            throw new CustomException(USER_ALREADY_ROLE, ErrorType.ALREADY_EXISTS);
        }
        user.getRoles().add(role);
    }

    @Override
    public AppUserDto getUser(String username) {
        log.info("Fetching user {}", username);
        AppUser appUser = userRepo.findByUsername(username);
        if (appUser == null) {
            throw new CustomException(USER_NOT_FOUND_NAME + username, ErrorType.NOT_FOUND);
        }
        return converter.convertAppUser(appUser);
    }

    @Override
    public List<AppUserDto> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll().stream()
                .map(appUser -> converter.convertValue(appUser, AppUserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto deleteRoleFromUser(String username, String roleName) {
        log.info("Deleting role {} from user {}", roleName, username);
        AppUser user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        if (user == null) {
            throw new CustomException(USER_NOT_FOUND_NAME + username, ErrorType.NOT_FOUND);
        }
        if (role == null) {
            throw new CustomException(ROLE_NOT_FOUND_NAME + roleName, ErrorType.NOT_FOUND);
        }
        user.getRoles().remove(role);
        return converter.convertValue(role, RoleDto.class);

    }

    @Override
    public AppUserDto deleteUser(String username) {
        AppUser user = userRepo.findByUsername(username);
        userRepo.deleteById(user.getId());
        return converter.convertValue(user, AppUserDto.class);
    }
}
