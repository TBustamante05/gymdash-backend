package com.example.gymdash.dtos.routines;

public record ExerciseRequest(
    String name,
    Integer sets,
    Integer reps,
    Double weight,
    Integer restTime
) {}
