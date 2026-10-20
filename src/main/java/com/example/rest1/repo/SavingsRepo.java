package com.example.rest1.repo;

import com.example.rest1.model.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsRepo extends JpaRepository<SavingsGoal,Long> {
}
