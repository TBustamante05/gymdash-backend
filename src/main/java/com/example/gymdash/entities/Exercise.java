package com.example.gymdash.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer sets;
    private Integer reps;
    private Double weight;
    private Integer restTime;

    // Relación con WorkoutRoutine: Muchos ejercicios pertenecen a una rutina
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private WorkoutRoutine routine;
}
