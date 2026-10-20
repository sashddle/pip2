package com.example.rest1.servic;

import com.example.rest1.model.*;
import com.example.rest1.pip.*;
import com.example.rest1.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Service
@Transactional
public class BudgetServ {
    private final BudgetRepo categories;
    private final UserRepo users;
    private final FinancialTransRepo transactions;
    public BudgetServ(BudgetRepo categories, UserRepo users, FinancialTransRepo transactions) {
        this.categories = categories;
        this.users = users;
        this.transactions = transactions;
    }
    public List<BudgetCategory> all() {
        return categories.findAll();
    }
    public BudgetCategory get(Long id) {
        return categories.findById(id == null ? -1L : id).orElseThrow(() -> Check.missing("Category"));
    }
    public BudgetCategory save(Long id, BudgetRequest request) {
        BudgetCategory category = id == null ? new BudgetCategory() : get(id);
        if (id == null || request.name()!=null) {
            Check.text(request.name());
            category.name = request.name();
        }
        if (id == null || request.monthlyExpenseLimit()!=null) {
            Check.nonnegative(request.monthlyExpenseLimit());
            category.monthlyExpenseLimit = request.monthlyExpenseLimit();
        }
        if (id == null || request.ownerId()!=null) {
            User owner = users.findById(request.ownerId()==null ? -1 : request.ownerId()).orElseThrow(() -> Check.missing("User"));
            boolean referenced = false;
            if (id != null) {
                for (FinancialTrans operation : transactions.findAll()) {
                    if (operation.category != null && operation.category.id.equals(id)) {
                        referenced = true;
                    }
                }
            }
            if (referenced) {
                Check.require(category.owner.id.equals(owner.id), "Cannot change owner of referenced category");
            }
            category.owner = owner;
        }
        return categories.save(category);
    }
    // Считаем расходы по категории за конкретный месяц.
    public BigDecimal spent(Long id,  int year, int month) {
        get(id);
        Check.require(month >= 1 && month <= 12, "Invalid month");
        BigDecimal total = BigDecimal.ZERO;
        for (FinancialTrans operation : transactions.findAll()) {
            if (operation.category == null || !operation.category.id.equals(id)) {
                continue;
            }
            if (operation.type == TransactionType.EXPENSE
                    && operation.status == TransactionStatus.CONFIRMED
                    && operation.createdAt.getYear() == year
                    && operation.createdAt.getMonthValue() == month) {
                total = total.add(operation.amount);
            }
        }
        return total;
    }

    public void delete(Long id) {
        get(id);
        for (FinancialTrans operation : new ArrayList<>(transactions.findAll())) {
            if (operation.category != null && operation.category.id.equals(id)) {
                transactions.delete(operation);
            }
        }
        categories.deleteById(id);
    }
}
