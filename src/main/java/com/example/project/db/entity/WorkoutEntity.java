package com.example.project.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "workouts")
public class WorkoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column
    String name;

    @ManyToMany(mappedBy = "workouts",
            fetch = FetchType.LAZY)
    @JsonIgnore
    List<ClientEntity> clients;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "workout")
    List<InstructorEntity> instructors;
}
