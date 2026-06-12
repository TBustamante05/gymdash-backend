package com.example.gymdash.repositories;

import com.example.gymdash.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByRoutineId(Long routineId);
    Optional<Exercise> findByIdAndRoutineId(Long id, Long routineId);
}
