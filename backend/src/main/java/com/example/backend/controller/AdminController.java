package com.example.backend.controller;

import com.example.backend.dto.branch.BranchRequest;
import com.example.backend.dto.branch.BranchResponse;
import com.example.backend.dto.categories.CategoriesRequest;
import com.example.backend.dto.categories.CategoriesResponse;
import com.example.backend.dto.product.ProductRequest;
import com.example.backend.dto.product.ProductResponse;
import com.example.backend.dto.user.AdminUserResponse;
import com.example.backend.dto.user.UpdateRoleRequest;
import com.example.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // --- Users ---

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<AdminUserResponse> updateUserRole(
            @PathVariable UUID userId,
            @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(adminService.updateUserRole(userId, request));
    }

    // --- Products ---

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(adminService.createProduct(request));
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductRequest request) {
        return ResponseEntity.ok(adminService.updateProduct(productId, request));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        adminService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // --- Categories ---

    @GetMapping("/categories")
    public ResponseEntity<List<CategoriesResponse>> getAllCategories() {
        return ResponseEntity.ok(adminService.getAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoriesResponse> createCategory(@RequestBody CategoriesRequest request) {
        return ResponseEntity.ok(adminService.createCategory(request));
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoriesResponse> updateCategory(
            @PathVariable UUID categoryId,
            @RequestBody CategoriesRequest request) {
        return ResponseEntity.ok(adminService.updateCategory(categoryId, request));
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
        adminService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    // --- Branches ---

    @PostMapping("/branches")
    public ResponseEntity<BranchResponse> createBranch(@RequestBody BranchRequest request) {
        return ResponseEntity.ok(adminService.createBranch(request));
    }

    @PutMapping("/branches/{branchId}")
    public ResponseEntity<BranchResponse> updateBranch(
            @PathVariable UUID branchId,
            @RequestBody BranchRequest request) {
        return ResponseEntity.ok(adminService.updateBranch(branchId, request));
    }

    @DeleteMapping("/branches/{branchId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID branchId) {
        adminService.deleteBranch(branchId);
        return ResponseEntity.noContent().build();
    }
}
