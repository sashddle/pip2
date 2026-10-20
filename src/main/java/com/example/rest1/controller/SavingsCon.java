package com.example.rest1.controller;

import com.example.rest1.servic.SavingServ;
import com.example.rest1.pip.SavingReq;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.util.*;

@RestController @RequestMapping("/api/savings-goals")
public class SavingsCon {
    private final SavingServ service;
    public SavingsCon(SavingServ service) {
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
    public ResponseEntity<?> create(@RequestBody SavingReq r)
    {
        return ResponseEntity.status(201).body(service.save(null,r));
    }
    @PatchMapping("/{id}")
    public Object update(@PathVariable Long id,@RequestBody SavingReq r) {
        return service.save(id,r);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    @GetMapping("/{id}/saved")
    public Object saved(@PathVariable Long id) {
        return Map.of("goalId",id,"savedAmount",service.saved(id));
    }
}
