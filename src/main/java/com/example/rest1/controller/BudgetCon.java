package com.example.rest1.controller;

import com.example.rest1.servic.BudgetServ;
import com.example.rest1.pip.BudgetRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.*;

@RestController @RequestMapping("/api/budget-categories")
public class BudgetCon {
    private final BudgetServ service;
    public BudgetCon(BudgetServ service) {
        this.service = service;
    }
    @GetMapping
    public Object all()
    {
        return service.all();
    }
    @GetMapping("/{id}")
    public Object one(@PathVariable Long id) {
        return service.get(id);
    }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody BudgetRequest r)
    {
        return ResponseEntity.status(201).body(service.save(null,r));
    }
    @PatchMapping("/{id}")
    public Object update(@PathVariable Long id,@RequestBody BudgetRequest r) {
        return service.save(id,r);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    @GetMapping("/{id}/spent")
    public Object spent(@PathVariable Long id,@RequestParam int year,@RequestParam int month) {
        if(month<1||month>12)throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid month");
        return Map.of("categoryId",id,"year",year,"month",month,"spent",service.spent(id,year,month));
    }
}
