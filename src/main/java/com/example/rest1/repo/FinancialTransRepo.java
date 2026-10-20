package com.example.rest1.repo;

import com.example.rest1.model.FinancialTrans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialTransRepo extends JpaRepository<FinancialTrans,Long> {
}
