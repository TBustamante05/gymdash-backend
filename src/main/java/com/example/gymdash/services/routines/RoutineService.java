package com.example.gymdash.services.routines;

import com.example.gymdash.dtos.routines.ExerciseRequest;
import com.example.gymdash.dtos.routines.ExerciseResponse;
import com.example.gymdash.dtos.routines.RoutineRequest;
import com.example.gymdash.dtos.routines.RoutineResponse;
import com.example.gymdash.entities.Exercise;
import com.example.gymdash.entities.User;
import com.example.gymdash.entities.WorkoutRoutine;
import com.example.gymdash.repositories.ExerciseRepository;
import com.example.gymdash.repositories.WorkoutRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final WorkoutRoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;

    // Obtener el usuario del token JWT
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    // Rutinas
    public List<RoutineResponse> getAllRoutines() {
        Long userId = getCurrentUser().getId();
        return routineRepository.findAllByUserId(userId)
                .stream()
                .map(this::toRoutineResponse)
                .toList();
    }

    public RoutineResponse getRoutineById(Long id) {
        Long userId = getCurrentUser().getId();
        WorkoutRoutine routine = routineRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));
        return toRoutineResponse(routine);
    }

    public RoutineResponse createRoutine(RoutineRequest req) {
        User user = getCurrentUser();
        WorkoutRoutine routine = WorkoutRoutine.builder()
                .name(req.name())
                .description(req.description())
                .user(user)
                .build();
        return toRoutineResponse(routineRepository.save(routine));
    }

    public RoutineResponse updateRoutine(Long id, RoutineRequest req) {
        Long userId = getCurrentUser().getId();
        WorkoutRoutine routine = routineRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        routine.setName(req.name());
        routine.setDescription(req.description());
        return toRoutineResponse(routineRepository.save(routine));
    }

    public void deleteRoutine(Long id) {
        Long userId = getCurrentUser().getId();
        WorkoutRoutine routine = routineRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));
        routineRepository.delete(routine);
    }

    // Ejercicios
    public List<ExerciseResponse> getExercises(Long routineId) {
        Long userId = getCurrentUser().getId();
        // Verificar que la rutina pertenece al usuario
        routineRepository.findByIdAndUserId(routineId, userId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        return exerciseRepository.findAllByRoutineId(routineId)
                .stream()
                .map(this::toExerciseResponse)
                .toList();
    }

    public ExerciseResponse addExercise(Long routineId, ExerciseRequest req) {
        Long userId = getCurrentUser().getId();
        WorkoutRoutine routine = routineRepository
                .findByIdAndUserId(routineId, userId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        Exercise exercise = Exercise.builder()
                .name(req.name())
                .sets(req.sets())
                .reps(req.reps())
                .restTime(req.restTime())
                .weight(req.weight())
                .routine(routine)
                .build();

        return toExerciseResponse(exerciseRepository.save(exercise));
    }

    public ExerciseResponse updateExercise(Long routineId, Long exerciseId, ExerciseRequest req) {
        Long userId = getCurrentUser().getId();
        routineRepository.findByIdAndUserId(routineId, userId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        Exercise exercise = exerciseRepository
                .findByIdAndRoutineId(exerciseId, routineId)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));

        exercise.setName(req.name());
        exercise.setSets(req.sets());
        exercise.setReps(req.reps());
        exercise.setWeight(req.weight());
        exercise.setRestTime(req.restTime());

        return toExerciseResponse(exerciseRepository.save(exercise));
    }

    public void deleteExercise(Long routineId, Long exerciseId) {
        Long userId = getCurrentUser().getId();
        routineRepository.findByIdAndUserId(routineId, userId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        Exercise exercise = exerciseRepository
                .findByIdAndRoutineId(exerciseId, routineId)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));

        exerciseRepository.delete(exercise);
    }
    // Mappers
    private RoutineResponse toRoutineResponse(WorkoutRoutine r) {
        List<ExerciseResponse> exercises = r.getExercises()
                .stream()
                .map(this::toExerciseResponse)
                .toList();

        return new RoutineResponse(
                r.getId(), r.getName(), r.getDescription(), r.getCreatedAt(), exercises
        );
    }

    private ExerciseResponse toExerciseResponse(Exercise e) {
        return new ExerciseResponse(
                e.getId(), e.getName(), e.getSets(), e.getReps(), e.getWeight(), e.getRestTime()
        );
    }
}
