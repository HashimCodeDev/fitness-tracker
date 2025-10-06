package com.example.fitnesstracker.controller;

import com.example.fitnesstracker.model.User;
import com.example.fitnesstracker.model.Workout;
import com.example.fitnesstracker.repository.UserRepository;
import com.example.fitnesstracker.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/workout-history")
@CrossOrigin(origins = "*")
public class WorkoutHistoryController {

    @Autowired
    private WorkoutRepository workoutRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getWorkoutHistory(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Workout> workoutPage = workoutRepository.findByUser(user, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("workouts", workoutPage.getContent());
        response.put("totalElements", workoutPage.getTotalElements());
        response.put("totalPages", workoutPage.getTotalPages());
        response.put("currentPage", page);
        response.put("pageSize", size);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Workout>> getRecentWorkouts(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "5") int limit) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Pageable pageable = PageRequest.of(0, limit, Sort.by("date").descending());
        Page<Workout> workouts = workoutRepository.findByUser(user, pageable);
        return ResponseEntity.ok(workouts.getContent());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Workout>> getWorkoutsByDateRange(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Workout> workouts = workoutRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate);
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/{workoutId}")
    public ResponseEntity<Workout> getWorkoutDetails(
            @PathVariable Long workoutId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Optional<Workout> workout = workoutRepository.findByIdAndUser(workoutId, user);
        return workout.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/summary")
    public ResponseEntity<WorkoutSummary> getWorkoutSummary(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Workout> workouts;
        if (startDate != null && endDate != null) {
            workouts = workoutRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate);
        } else {
            workouts = workoutRepository.findByUserOrderByDateDesc(user);
        }
        
        WorkoutSummary summary = calculateSummary(workouts);
        return ResponseEntity.ok(summary);
    }

    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Map<String, String>> deleteWorkout(
            @PathVariable Long workoutId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId) {
        
        User user = findUserByEmailOrId(email, userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }
        
        Optional<Workout> workout = workoutRepository.findByIdAndUser(workoutId, user);
        if (workout.isPresent()) {
            workoutRepository.delete(workout.get());
            return ResponseEntity.ok(Map.of("message", "Workout deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    private User findUserByEmailOrId(String email, Long userId) {
        if (email != null && !email.trim().isEmpty()) {
            return userRepository.findByEmail(email).orElse(null);
        } else if (userId != null) {
            return userRepository.findById(userId).orElse(null);
        }
        return null;
    }

    private WorkoutSummary calculateSummary(List<Workout> workouts) {
        WorkoutSummary summary = new WorkoutSummary();
        summary.setTotalWorkouts(workouts.size());
        
        int totalExercises = workouts.stream()
                .mapToInt(w -> w.getExercises() != null ? w.getExercises().size() : 0)
                .sum();
        summary.setTotalExercises(totalExercises);
        
        if (!workouts.isEmpty()) {
            summary.setMostRecentWorkout(workouts.get(0).getDate());
            summary.setOldestWorkout(workouts.get(workouts.size() - 1).getDate());
            
            // Calculate average workouts per week (last 4 weeks)
            LocalDate fourWeeksAgo = LocalDate.now().minusWeeks(4);
            long recentWorkouts = workouts.stream()
                    .filter(w -> w.getDate().isAfter(fourWeeksAgo))
                    .count();
            summary.setAverageWorkoutsPerWeek(recentWorkouts / 4.0);
        }
        
        return summary;
    }

    public static class WorkoutSummary {
        private int totalWorkouts;
        private int totalExercises;
        private LocalDate mostRecentWorkout;
        private LocalDate oldestWorkout;
        private double averageWorkoutsPerWeek;

        // Getters and setters
        public int getTotalWorkouts() { return totalWorkouts; }
        public void setTotalWorkouts(int totalWorkouts) { this.totalWorkouts = totalWorkouts; }
        public int getTotalExercises() { return totalExercises; }
        public void setTotalExercises(int totalExercises) { this.totalExercises = totalExercises; }
        public LocalDate getMostRecentWorkout() { return mostRecentWorkout; }
        public void setMostRecentWorkout(LocalDate mostRecentWorkout) { this.mostRecentWorkout = mostRecentWorkout; }
        public LocalDate getOldestWorkout() { return oldestWorkout; }
        public void setOldestWorkout(LocalDate oldestWorkout) { this.oldestWorkout = oldestWorkout; }
        public double getAverageWorkoutsPerWeek() { return averageWorkoutsPerWeek; }
        public void setAverageWorkoutsPerWeek(double averageWorkoutsPerWeek) { this.averageWorkoutsPerWeek = averageWorkoutsPerWeek; }
    }
}