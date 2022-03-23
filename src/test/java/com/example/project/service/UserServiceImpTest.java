package com.example.project.service;

import com.example.project.dto.AppUserDto;
import com.example.project.dto.RoleDto;
import com.example.project.entity.AppUser;
import com.example.project.entity.Role;
import com.example.project.exception.CustomException;
import com.example.project.repo.RoleRepo;
import com.example.project.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImpTest {

    @Autowired
    UserServiceImp userService;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    UserRepo userRepo;

    @BeforeEach
    void setUp(){
        userRepo.deleteAll();
    }

    @Test
    void shouldLoadUserDetailsByUsername() {
        AppUser user = new AppUser(null, "Bob", "dragon666", "zxczxc", new ArrayList<>());
        userRepo.save(user);

        UserDetails userDetails = userService.loadUserByUsername("dragon666");

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenLoadUserByUsername(){
        assertFalse(userRepo.existsByUsername("test"));

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("test"));

        assertEquals("User not found in the database", exception.getMessage());
    }

    @Test
    void saveUser() {
        AppUserDto user = new AppUserDto(null, "Bob", "dragon666", "zxczxc", new ArrayList<>());
        assertFalse(userRepo.existsByUsername(user.getUsername()));

        userService.saveUser(user);

        assertTrue(userRepo.existsByUsername(user.getUsername()));
    }

    @Test
    void shouldSaveRoleToDatabase() {
        RoleDto role = new RoleDto(null, "Test Role");
        assertNull(roleRepo.findByName("Test Role"));

        RoleDto saveRole = userService.saveRole(role);

        assertEquals(role.getName(), saveRole.getName());
        assertEquals(role.getName(), roleRepo.findByName("Test Role").getName());
        assertEquals(saveRole.getId(), roleRepo.findByName("Test Role").getId());
    }

    @Test
    @Transactional
    void shouldAddNewRoleToUser() {
        Role role = new Role(null, "new role");
        AppUser user = new AppUser(null, "Billy", "killer@", "asdasd", new ArrayList<>());
        userRepo.save(user);
        roleRepo.save(role);
        long userId = user.getId();

        userService.addRoleToUser("killer@", "new role");

        assertTrue(userRepo.getById(userId).getRoles().contains(role));
    }

    @Test
    void shouldGetUserByUsername() {
        AppUser user = new AppUser(null, "Billy", "killer@", "asdasd", new ArrayList<>());
        userRepo.save(user);

        AppUserDto userFromDb = userService.getUser("killer@");

        assertEquals(user.getUsername(), userFromDb.getUsername());
        assertEquals(user.getRoles().size(), userFromDb.getRoles().size());
        assertEquals(user.getPassword(), userFromDb.getPassword());
        assertEquals(user.getId(), userFromDb.getId());
    }

    @Test
    void shouldGetListOfUsers() {
        AppUser user1 = new AppUser(null, "Billy", "killer@", "asdasd", new ArrayList<>());
        AppUser user2 = new AppUser(null, "Bob", "dragon666", "zxczxc", new ArrayList<>());
        assertEquals(0, userRepo.findAll().size());
        userRepo.save(user1);
        userRepo.save(user2);

        List<AppUserDto> users = userService.getUsers();

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(appUserDto -> appUserDto.getUsername().equals("killer@")));
        assertTrue(users.stream().anyMatch(appUserDto -> appUserDto.getUsername().equals("dragon666")));
    }
}