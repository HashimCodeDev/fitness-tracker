package com.example.fitnesstracker.dto;

import lombok.Data;
import java.util.List;

@Data
public class WorkoutRequest {
    private String title;
    private List<ExerciseRequest> exercises;
    
    @Data
    public static class ExerciseRequest {
        private String name;
        private int sets;
        private int reps;
    }
}