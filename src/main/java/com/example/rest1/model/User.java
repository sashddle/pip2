package com.example.rest1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity @Table(name = "app_users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @NotBlank
    public String name;
    @Email @NotBlank @Column(unique = true)
    public String email;
}
