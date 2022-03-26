package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "class")
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "workout_name", unique = true)
    @NonNull
    private String name;
    private String description;
    @Column(name = "available")
    private boolean isAvailable;
    private Integer peopleLimit;

    @ManyToMany(mappedBy = "clientWorkouts",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JsonIgnore
    @ToString.Exclude
    private Set<ClientEntity> clients = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "instructor_id")
    @ToString.Exclude
    @JsonIgnore
    private InstructorEntity instructor;

    @OneToMany(cascade = CascadeType.ALL,
    mappedBy = "workoutClass",
    fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private Set<WorkoutSessionEntity> sessions;


    public WorkoutClassEntity(String name, boolean isAvailable, int peopleLimit) {
        this.name = name;
        this.isAvailable = isAvailable;
        this.peopleLimit = peopleLimit;
    }

    @Override
    public String toString() {
        return "ClassEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isAvailable=" + isAvailable +
                ", peopleLimit=" + peopleLimit +
                '}';
    }
}

