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

    public WorkoutSessionDto convertSessionEntity(WorkoutSessionEntity workoutSession) {
        return convertValue(workoutSession, WorkoutSessionDto.class);
    }

    public WorkoutSessionEntity convertSessionDto(WorkoutSessionDto workoutSessionDto) {
        return convertValue(workoutSessionDto, WorkoutSessionEntity.class);
    }

    public ClientDto convertClientEntity(ClientEntity clientEntity) {
        ClientDto clientDto = convertValue(clientEntity, ClientDto.class);
        Set<WorkoutClassDto> workouts = clientEntity.getClientWorkouts().stream()
                .map(workoutEntity -> convertValue(workoutEntity, WorkoutClassDto.class))
                .collect(Collectors.toSet());
        clientDto.setClientWorkouts(workouts);
        return clientDto;
    }

    public ClientEntity convertClientDto(ClientDto clientDto) {
        ClientEntity clientEntity = convertValue(clientDto, ClientEntity.class);

        Set<WorkoutClassEntity> workouts = clientDto.getClientWorkouts().stream()

                .map(workoutDto -> convertValue(workoutDto, WorkoutClassEntity.class))
                .collect(Collectors.toSet());
        clientEntity.setClientWorkouts(workouts);
        return clientEntity;
    }

    public InstructorDto convertInstructorEntity(InstructorEntity instructorEntity) {
        if (instructorEntity == null) {
            return null;
        }
        InstructorDto instructorDto = convertValue(instructorEntity, InstructorDto.class);
        Set<WorkoutClassDto> workouts = instructorEntity.getInstructorWorkouts().stream()
                .map(workoutEntity -> convertValue(workoutEntity, WorkoutClassDto.class))
                .collect(Collectors.toSet());
        instructorDto.setInstructorWorkouts(workouts);
        return instructorDto;
    }

    public InstructorEntity convertInstructorDto(InstructorDto instructorDto) {
        if (instructorDto == null) {
            return null;
        }
        InstructorEntity instructorEntity = convertValue(instructorDto, InstructorEntity.class);
        Set<WorkoutClassEntity> workouts = instructorDto.getInstructorWorkouts().stream()
                .map(workoutDto -> convertValue(workoutDto, WorkoutClassEntity.class))
                .collect(Collectors.toSet());
        instructorEntity.setInstructorWorkouts(workouts);
        return instructorEntity;
    }

    public WorkoutClassDto convertWorkoutClassEntity(WorkoutClassEntity workoutClassEntity) {
        WorkoutClassDto workoutClassDto = convertValue(workoutClassEntity, WorkoutClassDto.class);
        Set<ClientDto> clients = workoutClassEntity.getClients().stream()
                .map(this::convertClientEntity)
                .collect(Collectors.toSet());
        Set<WorkoutSessionDto> sessions = workoutClassEntity.getSessions().stream()
                .map(this::convertSessionEntity)
                .collect(Collectors.toSet());
        workoutClassDto.setSessions(sessions);
        workoutClassDto.setClients(clients);
        workoutClassDto.setInstructor(convertInstructorEntity(workoutClassEntity.getInstructor()));
        return workoutClassDto;
    }

    public WorkoutClassEntity convertWorkoutClassDto(WorkoutClassDto workoutClassDto) {
        WorkoutClassEntity workoutClassEntity = convertValue(workoutClassDto, WorkoutClassEntity.class);
        Set<ClientEntity> clients = workoutClassDto.getClients().stream()
                .map(this::convertClientDto)
                .collect(Collectors.toSet());
        workoutClassEntity.setClients(clients);
        workoutClassEntity.setInstructor(convertInstructorDto(workoutClassDto.getInstructor()));
        return workoutClassEntity;
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
