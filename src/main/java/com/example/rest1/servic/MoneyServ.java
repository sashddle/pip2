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
public class MoneyServ {
    private final MoneyRepo accounts;
    private final UserRepo users;
    private final FinancialTransRepo transactions;
    private final SavingsRepo goals;
    public MoneyServ(MoneyRepo accounts, UserRepo users, FinancialTransRepo transactions, SavingsRepo goals) {
        this.accounts = accounts;
        this.users = users;
        this.transactions = transactions;
        this.goals = goals;
    }
    public List<MoneyAcc> all() {
        return accounts.findAll();
    }
    public MoneyAcc get(Long id) {
        return accounts.findById(id == null ? -1L : id).orElseThrow(() -> Check.missing("Account"));
    }
    public MoneyAcc save(Long id,  MoneyReq request) {
        MoneyAcc account = id == null ? new MoneyAcc() : get(id);
        if (id == null || request.name() != null) {
            Check.text(request.name());
            account.name = request.name();
        }
        if (id == null || request.initialBalance() != null) {
            Check.nonnegative(request.initialBalance());
            account.initialBalance = request.initialBalance();
        }
        if (id == null || request.ownerId() != null) {
            User owner = users.findById(request.ownerId()==null ? -1 : request.ownerId()).orElseThrow(() -> Check.missing("User"));
            boolean referenced = false;
            for (FinancialTrans operation : transactions.findAll()) {
                if (operation.account.id.equals(id)) {
                    referenced = true;
                }
            }
            for (SavingsGoal goal : goals.findAll()) {
                if (goal.account.id.equals(id)) {
                    referenced = true;
                }
            }
            if (id != null && referenced) {
                Check.require(account.owner.id.equals(owner.id), "Cannot change owner of referenced account");
            }
            account.owner = owner;
        }
        return accounts.save(account);
    }
    public BigDecimal balance(Long id) {
        MoneyAcc a = get(id);
        BigDecimal balance = a.initialBalance;
        for (FinancialTrans t : transactions.findAll()) {
            if (!t.account.id.equals(id) || t.status != TransactionStatus.CONFIRMED) {
                continue;
            }
            if (t.type == TransactionType.INCOME) {
                balance = balance.add(t.amount);
            } else {
                balance = balance.subtract(t.amount);
            }
        }
        return balance;
    }
    public void delete(Long id) {
        get(id);
        for (FinancialTrans operation : new ArrayList<>(transactions.findAll())) {
            if (operation.account.id.equals(id)) {
                transactions.delete(operation);
            }
        }
        for (SavingsGoal goal : new ArrayList<>(goals.findAll())) {
            if (goal.account.id.equals(id)) {
                for (FinancialTrans operation : new ArrayList<>(transactions.findAll())) {
                    if (operation.goal != null && operation.goal.id.equals(goal.id)) {
                        transactions.delete(operation);
                    }
                }
                goals.delete(goal);
            }
        }
        accounts.deleteById(id);
    }
}
