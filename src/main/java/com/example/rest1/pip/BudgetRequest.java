package com.example.rest1.pip;

import java.math.BigDecimal;
import com.example.rest1.model.*;

public record BudgetRequest(String name, BigDecimal monthlyExpenseLimit, Long ownerId) {
}
