package com.example.fitnesstracker.repository;

import com.example.fitnesstracker.model.Workout;
import com.example.fitnesstracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserOrderByDateDesc(User user);
    Page<Workout> findByUser(User user, Pageable pageable);
    List<Workout> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);
    Optional<Workout> findByIdAndUser(Long id, User user);
}