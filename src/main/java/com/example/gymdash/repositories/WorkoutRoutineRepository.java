package com.example.gymdash.repositories;

import com.example.gymdash.entities.WorkoutRoutine;
import com.example.gymdash.exceptions.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutRoutineRepository extends JpaRepository<WorkoutRoutine, Long> {
    List<WorkoutRoutine> findAllByUserId(Long userId);

    // Seguridad para no acceder a rutinas ajenas
    Optional<WorkoutRoutine> findByIdAndUserId(Long id, Long userId);

    // Método default
    default WorkoutRoutine findByIdAndUserIdOrThrow(Long id, Long userId) {
        return findByIdAndUserId(id, userId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Rutina no encontrada"));
    }
}
