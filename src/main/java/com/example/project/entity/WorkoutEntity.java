package com.example.project.entity;

import com.example.project.exception.CustomException;
import com.example.project.exception.ErrorType;
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
    private long id;

    // TODO: 16.03.2022 remove redundant annotations
    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private int durationInMinutes;

    @Column(name = "available")
    private boolean isAvailable;

    @Column(name = "people_limit")
    private int peopleLimit;

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

    // TODO: 16.03.2022 remove logic from entity
    public void addInstructor(InstructorEntity instructor) {
        if (instructors.contains(instructor)) {
            throw new CustomException("Instructor " + instructor.getFirstName() + " already signed for this workout",
                    ErrorType.ALREADY_EXISTS);
        }
        instructors.add(instructor);
    }

    public void addClient(ClientEntity client) {
        if (clients.contains(client)) {
            throw new CustomException("Client " + client.getFirstName() + " already signed for this workout",
                    ErrorType.ALREADY_EXISTS);
        }
        if (showActiveClientsCounter() >= peopleLimit) {
            throw new CustomException("All free slots has been taken for this workout", ErrorType.ALREADY_EXISTS);
        }
        clients.add(client);
        client.getClientWorkouts().add(this);
    }

    public long showActiveClientsCounter() {
        return clients.stream().filter(ClientEntity::isActive).count();
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

