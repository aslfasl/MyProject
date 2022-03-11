package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private int durationInMinutes;

    @Column(name = "available")
    private boolean isAvailable;

    @Column(name = "people_limit")
    private int peopleLimit;

    @ManyToMany(mappedBy = "clientWorkouts",
            fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private Set<ClientEntity> clients = new HashSet<>();

    @ManyToMany(mappedBy = "instructorWorkouts",
            fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private Set<InstructorEntity> instructors = new HashSet<>();

    public WorkoutEntity(String name, int durationInMinutes, boolean isAvailable, int peopleLimit) {
        this.name = name;
        this.durationInMinutes = durationInMinutes;
        this.isAvailable = isAvailable;
        this.peopleLimit = peopleLimit;
    }

    public void addInstructor(InstructorEntity instructor){
        if (instructors.contains(instructor)){
            throw new RuntimeException("Такой инструктор уже есть. потом напишу"); // TODO: 07.03.2022
        }
        instructors.add(instructor);
    }

    public void addClient(ClientEntity client) {
        if (clients.contains(client)){
            throw new RuntimeException("Такой клиент уже есть.потом напишу"); // TODO: 07.03.2022
        }
        if (showActiveClientsCounter() >= peopleLimit){
            throw new RuntimeException("Все места заняты");
        }
        clients.add(client);
    }

    public long showActiveClientsCounter(){
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

    public static void main(String[] args) {
        WorkoutEntity workoutEntity = new WorkoutEntity("MyWorkout", 90, true, 3);
        ClientEntity clientEntity1 = new ClientEntity("Anna", "Smith", "1", LocalDate.now(), true);
        ClientEntity clientEntity2 = new ClientEntity("Foo", "Foofoo", "foofoofoo", LocalDate.now(), false);

        workoutEntity.addClient(clientEntity1);
        workoutEntity.addClient(clientEntity2);
        System.out.println(workoutEntity);


        System.out.println(workoutEntity.getClients().size());
        System.out.println(workoutEntity.showActiveClientsCounter());

    }
}

