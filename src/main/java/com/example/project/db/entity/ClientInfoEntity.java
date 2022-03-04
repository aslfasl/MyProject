package com.example.project.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "client_information")
public class ClientInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "address")
    String address;

    @Column(name = "start")
    LocalDate start;

    @Column(name = "expiration")
    LocalDate expiration;

    @OneToOne(mappedBy = "clientInfo",
            cascade = CascadeType.ALL)
    ClientEntity client;
}

