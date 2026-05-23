package com.example.backend.controller;

import com.example.backend.dto.branch.BranchRequest;
import com.example.backend.dto.branch.BranchResponse;
import com.example.backend.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchResponse> createBranch(@RequestBody BranchRequest request) {
        return ResponseEntity.ok(branchService.createBranch(request));
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getBranch(@PathVariable UUID id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }
}