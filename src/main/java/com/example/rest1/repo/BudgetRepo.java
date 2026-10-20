package com.example.rest1.repo;

import com.example.rest1.model.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepo extends JpaRepository<BudgetCategory,Long> {
}
