package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

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

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "status")
    @EqualsAndHashCode.Exclude
    private boolean isActive;

    public BaseEntity(String firstName, String lastName, String passport, LocalDate birthdate, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.birthdate = birthdate;
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return firstName.equals(that.firstName) && lastName.equals(that.lastName) && passport.equals(that.passport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, passport);
    }
}