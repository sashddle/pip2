package com.example.rest1.controller;

import com.example.rest1.servic.FinancialServ;
import com.example.rest1.pip.FinancialReq;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController @RequestMapping("/api/financial-transactions")
public class FinancialTransCon {
    private final FinancialServ service;
    public FinancialTransCon(FinancialServ service) {
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
    public ResponseEntity<?> create(@RequestBody FinancialReq r)
    {
        return ResponseEntity.status(201).body(service.save(null,r));
    }
    @PatchMapping("/{id}")
    public Object update(@PathVariable Long id,@RequestBody FinancialReq r) {
        return service.save(id,r);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
