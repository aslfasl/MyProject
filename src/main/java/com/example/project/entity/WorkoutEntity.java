package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "workout")
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column(name = "workout_name", unique = true)
    @NonNull
    private String name;
    @Column(name = "duration")
    private Integer durationInMinutes;
    @Column(name = "available")
    private boolean isAvailable;
    private Integer peopleLimit;

    @ManyToMany(mappedBy = "clientWorkouts",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JsonIgnore
    @ToString.Exclude
    private Set<ClientEntity> clients = new HashSet<>();

    @ManyToMany(mappedBy = "instructorWorkouts",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @ToString.Exclude
    @JsonIgnore
    private Set<InstructorEntity> instructors = new HashSet<>();

    public WorkoutEntity(String name, int durationInMinutes, boolean isAvailable, int peopleLimit) {
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.isAvailable = isAvailable;
        this.peopleLimit = peopleLimit;
    }

    @Override
    public String toString() {
        return "WorkoutEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", durationInMinutes=" + durationInMinutes +
                ", isAvailable=" + isAvailable +
                ", peopleLimit=" + peopleLimit +
                '}';
    }
}

