package com.example.rest1.servic;

import com.example.rest1.model.*;
import com.example.rest1.pip.*;
import com.example.rest1.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServ {
    private final UserRepo users;
    private final MoneyServ accounts;
    private final BudgetServ categories;
    private final SavingServ goals;
    public UserServ(UserRepo users, MoneyServ accounts, BudgetServ categories, SavingServ goals) {
        this.users = users;
        this.accounts = accounts;
        this.categories = categories;
        this.goals = goals;
    }
    public List<User> all() {
        return users.findAll();
    }
    public User get(Long id) {
        return users.findById(id == null ? -1L : id).orElseThrow(() -> Check.missing("User"));
    }
    public User save(Long id,  Userreq request) {
        User user = id == null ? new User() : get(id);
        if (id == null || request.name() != null) {
            Check.text(request.name());
            user.name = request.name();
        }
        if (id == null || request.email() != null) {
            Check.text(request.email());
            user.email = request.email();
        }
        return users.save(user);
    }
    public void delete(Long id) {
        get(id);
        for (MoneyAcc account : new ArrayList<>(accounts.all())) {
            if (account.owner.id.equals(id)) {
                accounts.delete(account.id);
            }
        }
        for (BudgetCategory category : new ArrayList<>(categories.all())) {
            if (category.owner.id.equals(id)) {
                categories.delete(category.id);
            }
        }
        for (SavingsGoal goal : new ArrayList<>(goals.all())) {
            if (goal.owner.id.equals(id)) {
                goals.delete(goal.id);
            }
        }
        users.deleteById(id);
    }
}
