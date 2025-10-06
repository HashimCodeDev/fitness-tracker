package com.example.fitnesstracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    @Email
    @Column(unique = true)
    private String email;
    
    @NotBlank
    private String password;
    
    private Integer age;
    private Double height; // in cm
    private Double weight; // in kg
    private Double bmi;
    
    public void calculateBmi() {
        if (height != null && weight != null && height > 0) {
            double heightInMeters = height / 100.0;
            this.bmi = weight / (heightInMeters * heightInMeters);
        }
    }
}