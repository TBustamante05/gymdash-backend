package com.example.gymdash.dtos.routines;

public record ExerciseResponse(
    Long id,
    String name,
    Integer sets,
    Integer reps,
    Double weight,
    Integer restTime,
    Integer position
) {}
