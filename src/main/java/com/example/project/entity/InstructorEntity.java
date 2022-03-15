package com.example.project.entity;

import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "instructor")
@NoArgsConstructor
@Getter
@Setter
public class InstructorEntity extends BaseEntity{

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name = "instructor_workout",
            joinColumns = {@JoinColumn(name = "instructor_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "workout_id", referencedColumnName = "id")})
    @ToString.Exclude
    private Set<WorkoutEntity> instructorWorkouts = new HashSet<>();

    public InstructorEntity(String firstName, String lastName, String passport, LocalDate birthdate, boolean isActive) {
        super(firstName, lastName, passport, birthdate, isActive);
    }

    public void addWorkout(WorkoutEntity workout){
        if (instructorWorkouts.contains(workout)) {
            throw new CustomException("This instructor already signed for: " + workout.getName(), ErrorType.ALREADY_EXISTS);
        }
        instructorWorkouts.add(workout);
        workout.getInstructors().add(this);
    }
}
