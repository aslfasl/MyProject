package com.example.project.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "instructors")
public class InstructorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "birthdate")
    LocalDate birthdate;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "workout_id")
    WorkoutEntity workout;

    @OneToOne(cascade = CascadeType.ALL)
            @JoinColumn(name = "info_id")
    InstructorInfoEntity instructorInfo;

}
