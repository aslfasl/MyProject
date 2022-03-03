package com.example.project.db.entity;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public class BaseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "address")
    String address;
}
