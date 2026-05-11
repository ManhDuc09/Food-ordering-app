package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "branches")
@Getter 
@Setter
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "branch_id", updatable = false, nullable = false)
    private UUID branchId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "is_open")
    private Boolean isOpen = true;


    @Column(name = "latitude")
    private java.math.BigDecimal latitude;

    @Column(name = "longitude")
    private java.math.BigDecimal longitude;
    public Branch() {}
}