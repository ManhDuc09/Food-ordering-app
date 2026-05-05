package com.example.backend.service;

import com.example.backend.dto.branch.BranchRequest;
import com.example.backend.dto.branch.BranchResponse;
import com.example.backend.mapper.BranchMapper;
import com.example.backend.model.Branch;
import com.example.backend.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor 
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public BranchResponse createBranch(BranchRequest request) {
        Branch branch = branchMapper.requestToBranch(request);
        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.branchToResponse(savedBranch);
    }

    public List<BranchResponse> getAllBranches() {
        return branchRepository.findAll()
                .stream()
                .map(branchMapper::branchToResponse)
                .toList();
    }

    public BranchResponse getBranchById(UUID id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        return branchMapper.branchToResponse(branch);
    }
}