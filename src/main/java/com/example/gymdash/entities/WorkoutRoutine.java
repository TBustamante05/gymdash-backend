package com.example.gymdash.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "workout_routines")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutRoutine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(updatable = false)
    private Date createdAt;

    // Relación con User: Muchas rutinas pertenecen a un usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relación con Exercise: Una rutina tiene muchos ejercicios
    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Exercise> exercises = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
