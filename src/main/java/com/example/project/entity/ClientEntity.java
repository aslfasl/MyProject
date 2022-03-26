package com.example.project.entity;

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
@NoArgsConstructor
@Table(name = "client")
@AllArgsConstructor
public class ClientEntity extends BaseEntity{

    @OneToOne(mappedBy = "client",
            cascade = {CascadeType.ALL})
    private MembershipEntity membership;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name = "client_workout",
            joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "workout_id", referencedColumnName = "id")})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<WorkoutClassEntity> clientWorkouts = new HashSet<>();

    public ClientEntity(String firstName, String lastName, String passport, LocalDate birthdate) {
        super(firstName, lastName, passport, birthdate);
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
