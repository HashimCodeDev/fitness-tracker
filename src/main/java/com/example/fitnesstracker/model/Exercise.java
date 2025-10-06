package com.example.fitnesstracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private int sets;
    private int reps;
    
    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;
}