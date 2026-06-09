package com.example.backend.service;

import com.example.backend.dto.branch.BranchRequest;
import com.example.backend.dto.branch.BranchResponse;
import com.example.backend.dto.categories.CategoriesRequest;
import com.example.backend.dto.categories.CategoriesResponse;
import com.example.backend.dto.product.ProductRequest;
import com.example.backend.dto.product.ProductResponse;
import com.example.backend.dto.user.AdminUserResponse;
import com.example.backend.dto.user.UpdateRoleRequest;
import com.example.backend.mapper.BranchMapper;
import com.example.backend.mapper.CategoriesMapper;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.model.Branch;
import com.example.backend.model.Categories;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.BranchRepository;
import com.example.backend.repository.CategoriesRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoriesRepository categoriesRepository;
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final ProductMapper productMapper;
    private final CategoriesMapper categoriesMapper;

    // --- User Management ---

    @Transactional(readOnly = true)
    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toAdminUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdminUserResponse updateUserRole(UUID userId, UpdateRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(request.getRole());

        if (request.getBranchId() != null) {
            Branch branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            user.setBranch(branch);
        } else {
            user.setBranch(null);
        }

        return toAdminUserResponse(userRepository.save(user));
    }

    // --- Product Management ---

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);
        product.setPrice(request.getPrice());
        product.setCategories(resolveCategories(request.getCategoryIds()));
        return productMapper.productToResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (request.getName() != null)
            product.setName(request.getName());
        if (request.getDescription() != null)
            product.setDescription(request.getDescription());
        if (request.getImageUrl() != null)
            product.setImageUrl(request.getImageUrl());
        if (request.getIsAvailable() != null)
            product.setIsAvailable(request.getIsAvailable());
        if (request.getPrice() != null)
            product.setPrice(request.getPrice());
        if (request.getCategoryIds() != null)
            product.setCategories(resolveCategories(request.getCategoryIds()));

        return productMapper.productToResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(productId);
    }

    // --- Category Management ---

    @Transactional(readOnly = true)
    public List<CategoriesResponse> getAllCategories() {
        return categoriesRepository.findAll().stream()
                .map(categoriesMapper::categoriesToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriesResponse createCategory(CategoriesRequest request) {
        Categories category = new Categories();
        category.setName(request.getName());
        category.setImageUrl(request.getImageUrl());
        return categoriesMapper.categoriesToResponse(categoriesRepository.save(category));
    }

    @Transactional
    public CategoriesResponse updateCategory(UUID categoryId, CategoriesRequest request) {
        Categories category = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (request.getName() != null)
            category.setName(request.getName());
        if (request.getImageUrl() != null)
            category.setImageUrl(request.getImageUrl());

        return categoriesMapper.categoriesToResponse(categoriesRepository.save(category));
    }

    @Transactional
    public void deleteCategory(UUID categoryId) {
        Categories category = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        // Remove from all products first to avoid join table FK violation
        Set<Product> linked = category.getProducts();
        linked.forEach(product -> product.getCategories().remove(category));
        productRepository.saveAll(linked);
        categoriesRepository.delete(category);
    }

    // --- Branch Management ---

    @Transactional
    public BranchResponse createBranch(BranchRequest request) {
        Branch branch = branchMapper.requestToBranch(request);
        return branchMapper.branchToResponse(branchRepository.save(branch));
    }

    @Transactional
    public BranchResponse updateBranch(UUID branchId, BranchRequest request) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        if (request.getName() != null)
            branch.setName(request.getName());
        if (request.getAddress() != null)
            branch.setAddress(request.getAddress());
        if (request.getLatitude() != null)
            branch.setLatitude(BigDecimal.valueOf(request.getLatitude()));
        if (request.getLongitude() != null)
            branch.setLongitude(BigDecimal.valueOf(request.getLongitude()));
        if (request.getIsOpen() != null)
            branch.setIsOpen(request.getIsOpen());

        return branchMapper.branchToResponse(branchRepository.save(branch));
    }

    @Transactional
    public void deleteBranch(UUID branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        // Unassign any managers on this branch
        List<User> managers = userRepository.findByBranch(branch);
        managers.forEach(u -> u.setBranch(null));
        userRepository.saveAll(managers);
        branchRepository.delete(branch);
    }

    // --- Helpers ---

    private Set<Categories> resolveCategories(Set<UUID> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty())
            return new HashSet<>();
        return categoryIds.stream()
                .map(id -> categoriesRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + id)))
                .collect(Collectors.toSet());
    }

    private AdminUserResponse toAdminUserResponse(User user) {
        AdminUserResponse dto = new AdminUserResponse();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        if (user.getBranch() != null) {
            dto.setBranchId(user.getBranch().getBranchId());
            dto.setBranchName(user.getBranch().getName());
        }
        return dto;
    }
}
