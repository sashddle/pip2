package com.example.rest1.pip;

import java.math.BigDecimal;
import com.example.rest1.model.*;

public record FinancialReq(BigDecimal amount, TransactionType type, TransactionStatus status, Long accountId, Long categoryId, Long goalId) {
}
