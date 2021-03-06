package com.example.project.entity;

import lombok.*;

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
    private Long id;

    private String firstName;
    private String lastName;
    @Column(name = "passport",
            unique = true)
    @NonNull
    private String passport;
    private String address;
    private String phone;
    private LocalDate birthdate;


    public BaseEntity(String firstName, String lastName, @NonNull String passport, LocalDate birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.birthdate = birthdate;
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
