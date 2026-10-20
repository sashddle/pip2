package com.example.rest1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity @Table(name = "financial_transactions")
public class FinancialTrans {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @NotNull @DecimalMin(value = "0.0",inclusive = false)
    public BigDecimal amount;
    @NotNull @Enumerated(EnumType.STRING)
    public TransactionType type;
    @NotNull @Enumerated(EnumType.STRING)
    public TransactionStatus status;
    public LocalDateTime createdAt;
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    public MoneyAcc account;
    @ManyToOne @JoinColumn(name = "category_id")
    public BudgetCategory category;
    @ManyToOne @JoinColumn(name = "goal_id")
    public SavingsGoal goal;
}
