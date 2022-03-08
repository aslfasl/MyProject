package com.example.project.dto;

import com.example.project.entity.ClientEntity;
import com.example.project.entity.WorkoutEntity;
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
        List<WorkoutDto> workouts = clientEntity.getClientWorkouts().stream()
                .map(workoutEntity -> convertValue(workoutEntity, WorkoutDto.class))
                .collect(Collectors.toList());
        clientDto.setWorkouts(workouts);
        return clientDto;
    }

    public ClientEntity convertClientDto(ClientDto clientDto) {
        ClientEntity clientEntity = convertValue(clientDto, ClientEntity.class);

        Set<WorkoutEntity> workouts = clientDto.getWorkouts().stream()

                .map(workoutDto -> convertValue(workoutDto, WorkoutEntity.class))
                .collect(Collectors.toSet());
        clientEntity.setClientWorkouts(workouts);
        return clientEntity;
    }
}
