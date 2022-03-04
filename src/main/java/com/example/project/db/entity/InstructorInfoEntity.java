package com.example.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "instructor_information")
public class InstructorInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "address")
    String address;

    @Column(name = "education")
    String education;

    @Column(name = "salary")
    int salary;

    @Column(name = "category")
    int category;

    @OneToOne(mappedBy = "instructorInfo",
            cascade = CascadeType.ALL)
    InstructorEntity instructor;

}
