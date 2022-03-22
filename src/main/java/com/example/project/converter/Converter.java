package com.example.project.converter;

import com.example.project.dto.*;
import com.example.project.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Converter {

    @Setter
    private ObjectMapper objectMapper;

    public <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return objectMapper.convertValue(fromValue, toValueType);
    }

    public ClientDto convertClientEntity(ClientEntity clientEntity) {
        ClientDto clientDto = convertValue(clientEntity, ClientDto.class);
        Set<WorkoutDto> workouts = clientEntity.getClientWorkouts().stream()
                .map(workoutEntity -> convertValue(workoutEntity, WorkoutDto.class))
                .collect(Collectors.toSet());
        clientDto.setClientWorkouts(workouts);
        return clientDto;
    }

    public ClientEntity convertClientDto(ClientDto clientDto) {
        ClientEntity clientEntity = convertValue(clientDto, ClientEntity.class);

        Set<WorkoutEntity> workouts = clientDto.getClientWorkouts().stream()

                .map(workoutDto -> convertValue(workoutDto, WorkoutEntity.class))
                .collect(Collectors.toSet());
        clientEntity.setClientWorkouts(workouts);
        return clientEntity;
    }

    public InstructorDto convertInstructorEntity(InstructorEntity instructorEntity) {
        InstructorDto instructorDto = convertValue(instructorEntity, InstructorDto.class);
        Set<WorkoutDto> workouts = instructorEntity.getInstructorWorkouts().stream()
                .map(workoutEntity -> convertValue(workoutEntity, WorkoutDto.class))
                .collect(Collectors.toSet());
        instructorDto.setInstructorWorkouts(workouts);
        return instructorDto;
    }

    public InstructorEntity convertInstructorDto(InstructorDto instructorDto) {
        InstructorEntity instructorEntity = convertValue(instructorDto, InstructorEntity.class);
        Set<WorkoutEntity> workouts = instructorDto.getInstructorWorkouts().stream()
                .map(workoutDto -> convertValue(workoutDto, WorkoutEntity.class))
                .collect(Collectors.toSet());
        instructorEntity.setInstructorWorkouts(workouts);
        return instructorEntity;
    }

    public WorkoutDto convertWorkoutEntity(WorkoutEntity workoutEntity) {
        WorkoutDto workoutDto = convertValue(workoutEntity, WorkoutDto.class);
        Set<ClientDto> clients = workoutEntity.getClients().stream()
                .map(this::convertClientEntity)
                .collect(Collectors.toSet());
        Set<InstructorDto> instructors = workoutEntity.getInstructors().stream()
                .map(this::convertInstructorEntity)
                .collect(Collectors.toSet());
        workoutDto.setClients(clients);
        workoutDto.setInstructors(instructors);
        return workoutDto;
    }

    public WorkoutEntity convertWorkoutDto(WorkoutDto workoutDto) {
        WorkoutEntity workoutEntity = convertValue(workoutDto, WorkoutEntity.class);
        Set<ClientEntity> clients = workoutDto.getClients().stream()
                .map(this::convertClientDto)
                .collect(Collectors.toSet());
        Set<InstructorEntity> instructors = workoutDto.getInstructors().stream()
                .map(this::convertInstructorDto)
                .collect(Collectors.toSet());
        workoutEntity.setClients(clients);
        workoutEntity.setInstructors(instructors);
        return workoutEntity;
    }

    public AppUserDto convertAppUser(AppUser appUser) {
        AppUserDto userDto = convertValue(appUser, AppUserDto.class);
        List<RoleDto> roleDto = appUser.getRoles().stream()
                .map(role -> convertValue(role, RoleDto.class))
                .collect(Collectors.toList());
        userDto.setRoles(roleDto);
        return userDto;
    }

    public AppUser convertAppUserDto(AppUserDto appUserDto) {
        AppUser appUser = convertValue(appUserDto, AppUser.class);
        List<Role> roles = appUserDto.getRoles().stream()
                .map(role -> convertValue(role, Role.class))
                .collect(Collectors.toList());
        appUser.setRoles(roles);
        return appUser;
    }
}
