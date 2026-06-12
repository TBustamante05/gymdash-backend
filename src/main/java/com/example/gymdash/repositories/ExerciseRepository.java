package com.example.gymdash.repositories;

import com.example.gymdash.entities.Exercise;
import com.example.gymdash.exceptions.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByRoutineId(Long routineId);
    Optional<Exercise> findByIdAndRoutineId(Long id, Long routineId);

    // Método default
    default Exercise findByIdAndRoutineIdOrThrow(Long id, Long routineId) {
        return findByIdAndRoutineId(id, routineId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Ejercicio no encontrado"));
    }
}
