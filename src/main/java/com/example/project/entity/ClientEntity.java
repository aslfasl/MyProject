package com.example.project.entity;

import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "client")
@AllArgsConstructor
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "passport",
            unique = true)
    private String passport;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "status")
    @EqualsAndHashCode.Exclude
    private boolean isActive;

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
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.birthdate = birthdate;
        this.isActive = isActive;
    }

    public void addWorkout(WorkoutEntity workout) {
        if (clientWorkouts.contains(workout)) {
            throw new CustomException("This client already signed for: " + workout.getName(), ErrorType.ALREADY_EXISTS);
        }
        clientWorkouts.add(workout);
        workout.getClients().add(this);
    }
}
