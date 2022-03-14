package com.example.project.entity;

import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "instructor")
@NoArgsConstructor
public class InstructorEntity {

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

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name = "instructor_workout",
            joinColumns = {@JoinColumn(name = "instructor_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "workout_id", referencedColumnName = "id")})
    @ToString.Exclude
    private Set<WorkoutEntity> instructorWorkouts = new HashSet<>();

    public InstructorEntity(String firstName, String lastName, String passport, boolean isActive, LocalDate birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.isActive = isActive;
        this.birthdate = birthdate;
    }

    public void addWorkout(WorkoutEntity workout){
        if (instructorWorkouts.contains(workout)) {
            throw new CustomException("This instructor already signed for: " + workout.getName(), ErrorType.ALREADY_EXISTS);
        }
        instructorWorkouts.add(workout);
        workout.getInstructors().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstructorEntity that = (InstructorEntity) o;
        return firstName.equals(that.firstName) && lastName.equals(that.lastName) && birthdate.equals(that.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthdate);
    }

    @Override
    public String toString() {
        return "InstructorEntity{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
