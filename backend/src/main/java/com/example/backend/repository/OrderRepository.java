package com.example.backend.repository;

import com.example.backend.model.Branch;
import com.example.backend.model.Order;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    Page<Order> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    List<Order> findByBranchOrderByCreatedAtDesc(Branch branch);
}
