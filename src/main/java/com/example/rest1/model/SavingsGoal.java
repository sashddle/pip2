package com.example.rest1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity @Table(name = "savings_goals")
public class SavingsGoal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @NotBlank
    public String name;
    @NotNull @DecimalMin(value = "0.0",inclusive = false)
    public BigDecimal targetAmount;
    @NotBlank
    public String status;
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    public User owner;
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    public MoneyAcc account;
}
