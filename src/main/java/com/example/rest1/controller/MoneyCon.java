package com.example.rest1.controller;

import com.example.rest1.servic.MoneyServ;
import com.example.rest1.pip.MoneyReq;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.*;

@RestController @RequestMapping("/api/money-accounts")
public class MoneyCon {
    private final MoneyServ service;
    public MoneyCon(MoneyServ service) {
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
    public ResponseEntity<?> create(@RequestBody MoneyReq r)
    {
        return ResponseEntity.status(201).body(service.save(null,r));
    }
    @PatchMapping("/{id}")
    public Object update(@PathVariable Long id,@RequestBody MoneyReq r) {
        return service.save(id,r);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    @GetMapping("/{id}/balance")
    public Object balance(@PathVariable Long id) {
        return Map.of("accountId",id,"balance",service.balance(id));
    }
}
