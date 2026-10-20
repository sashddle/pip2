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
public class FinancialServ {
    private final FinancialTransRepo transactions;
    private final MoneyRepo accounts;
    private final BudgetRepo categories;
    private final SavingsRepo goals;
    public FinancialServ(FinancialTransRepo transactions,MoneyRepo accounts,BudgetRepo categories,SavingsRepo goals) {
        this.transactions = transactions;
        this.accounts = accounts;
        this.categories = categories;
        this.goals = goals;
    }
    public List<FinancialTrans> all() {
        return transactions.findAll();
    }
    public FinancialTrans get(Long id) {
        return transactions.findById(id == null ? -1L : id).orElseThrow(() -> Check.missing("Transaction"));
    }
    public FinancialTrans save(Long id, FinancialReq request) {
        FinancialTrans operation = id == null ? new FinancialTrans() : get(id);
        if (id == null || request.amount()!=null) {
            Check.positive(request.amount());
            operation.amount = request.amount();
        }
        if (id == null || request.type()!=null) {
            Check.require(request.type()!=null,"Type required");
            operation.type = request.type();
        }
        if (id == null || request.status()!=null) {
            Check.require(request.status()!=null,"Status required");
            operation.status = request.status();
        }
        if (id == null || request.accountId()!=null) {
            operation.account = accounts.findById(request.accountId()==null ? -1 : request.accountId()).orElseThrow(() -> Check.missing("Account"));
        }
        // A missing optional ID on PATCH keeps its existing value;
        // Changing the type clears incompatible relations.
        if (id == null || request.type()!=null) {
            if (operation.type==TransactionType.INCOME) {
                operation.category = null;
                operation.goal = null;
            }
            if (operation.type==TransactionType.EXPENSE)operation.goal = null;
            if (operation.type==TransactionType.CONTRIBUTION)operation.category = null;
        }
        if (request.categoryId()!=null)operation.category = categories.findById(request.categoryId()).orElseThrow(() -> Check.missing("Category"));
        if (request.goalId()!=null)operation.goal = goals.findById(request.goalId()).orElseThrow(() -> Check.missing("Goal"));
        Check.require(operation.type!=TransactionType.EXPENSE || (operation.category!=null && operation.goal==null),"Expense requires category and no goal");
        Check.require(operation.type!=TransactionType.CONTRIBUTION || (operation.goal!=null && operation.category==null),"Contribution requires goal and no category");
        Check.require(operation.type!=TransactionType.INCOME || (operation.category==null && operation.goal==null),"Income cannot have category or goal");
        if (operation.category!=null)Check.require(operation.category.owner.id.equals(operation.account.owner.id),"Category belongs to another owner");
        if (operation.goal!=null)Check.require(operation.goal.account.id.equals(operation.account.id),"Contribution must use goal account");
        if (id == null)operation.createdAt = LocalDateTime.now();
        return transactions.save(operation);
    }
    public void delete(Long id) {
        get(id);
        transactions.deleteById(id);
    }
}
