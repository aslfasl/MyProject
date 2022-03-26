package com.example.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "membership_info")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;

    @Column(name = "status")
    @EqualsAndHashCode.Exclude
    private boolean isActive;

    @OneToOne(mappedBy = "membership",
            cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ClientEntity client;

    public MembershipEntity(LocalDate startDate, LocalDate endDate, boolean isActive) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }
}
