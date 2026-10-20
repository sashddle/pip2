package com.example.rest1.repo;

import com.example.rest1.model.MoneyAcc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyRepo extends JpaRepository<MoneyAcc,Long> {
}
