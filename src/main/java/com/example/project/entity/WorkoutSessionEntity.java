package com.example.project.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "session")
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;
    @Column(name = "duration")
    private Duration durationInMinutes;
    private LocalDate startDate;
    private LocalTime startTime;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    @EqualsAndHashCode.Exclude
    private WorkoutClassEntity workoutClass;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutSessionEntity that = (WorkoutSessionEntity) o;
        return Objects.equals(durationInMinutes, that.durationInMinutes) && Objects.equals(startDate, that.startDate) && Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(durationInMinutes, startDate, startTime);
    }
}
