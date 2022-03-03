package com.example.project.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "client_information")
public class ClientInfoEntity extends BaseInfo {

    @Column(name = "start")
    LocalDate start;

    @Column(name = "expiration")
    LocalDate expiration;

    @OneToOne(mappedBy = "clientInfo",
            cascade = CascadeType.ALL)
    ClientEntity client;
}

