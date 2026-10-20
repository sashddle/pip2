package com.example.rest1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity @Table(name = "budget_categories")
public class BudgetCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @NotBlank
    public String name;
    @NotNull @DecimalMin("0.0")
    public BigDecimal monthlyExpenseLimit;
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    public User owner;
}
