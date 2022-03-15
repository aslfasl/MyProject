package com.example.project.entity;

import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "client")
@AllArgsConstructor
public class ClientEntity extends BaseEntity{

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name = "client_workout",
            joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "workout_id", referencedColumnName = "id")})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<WorkoutEntity> clientWorkouts = new HashSet<>();

    public ClientEntity(String firstName, String lastName, String passport, LocalDate birthdate, boolean isActive) {
        super(firstName, lastName, passport, birthdate, isActive);
    }


    public void addWorkout(WorkoutEntity workout) {
        if (clientWorkouts.contains(workout)) {
            throw new CustomException("This client already signed for: " + workout.getName(), ErrorType.ALREADY_EXISTS);
        }
        clientWorkouts.add(workout);
        workout.getClients().add(this);
    }
}
