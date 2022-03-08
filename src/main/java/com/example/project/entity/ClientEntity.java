package com.example.project.entity;

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
    long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "passport",
            unique = true)
    String passport;

    @Column(name = "birthdate")
    LocalDate birthdate;

    @Column(name = "status")
    boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name = "client_workout",
            joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "workout_id", referencedColumnName = "id")})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<WorkoutEntity> clientWorkouts = new HashSet<>();


    public ClientEntity(String firstName, String lastName, String passport, LocalDate birthdate, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.birthdate = birthdate;
        this.isActive = isActive;
    }

    public void addWorkout(WorkoutEntity workout) {
        if (clientWorkouts.contains(workout)) {
            throw new RuntimeException("тут должен быть кастомный эксепшн"); // TODO: 07.03.2022
        }
        clientWorkouts.add(workout);
    }


}
