package com.example.backend.mapper;

import com.example.backend.model.Branch;
import com.example.backend.dto.branch.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    
    BranchResponse branchToResponse(Branch branch);


    @Mapping(target = "branchId", ignore = true)
    Branch requestToBranch(BranchRequest request);
}