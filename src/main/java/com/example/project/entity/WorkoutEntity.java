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
public class WorkoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    long id;

    @Column(name = "name")
    String name;

    @Column(name = "duration")
    int duration;

    @ManyToMany(mappedBy = "clientWorkouts",
            fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    Set<ClientEntity> clients = new HashSet<>();

    @ManyToMany(mappedBy = "instructorWorkouts",
            fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    Set<InstructorEntity> instructors = new HashSet<>();

    public WorkoutEntity(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public void addInstructor(InstructorEntity instructor){
        if (instructors.contains(instructor)){
            throw new RuntimeException("потом напишу"); // TODO: 07.03.2022
        }
        instructors.add(instructor);
    }

    public void addClient(ClientEntity client) {
        if (clients.contains(client)){
            throw new RuntimeException("потом напишу"); // TODO: 07.03.2022
        }
        clients.add(client);
    }

    @Override
    public String toString() {
        return "WorkoutEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
