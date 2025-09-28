package com.example.fitnesstracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "workouts")
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workoutName;
    private String description;
    private int durationMinutes; // workout duration
}