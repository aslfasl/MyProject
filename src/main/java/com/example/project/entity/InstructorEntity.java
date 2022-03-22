package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return super.getFirstName().equals(that.getFirstName()) && super.getLastName().equals(that.getLastName())
                && super.getPassport().equals(that.getPassport());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getFirstName(), this.getLastName(), this.getPassport());
    }
}
