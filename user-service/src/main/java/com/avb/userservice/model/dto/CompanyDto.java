package com.avb.userservice.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record CompanyDto(
    Long id,
    @NotBlank(message = "Company name is required")
    String companyName,
    Double budget,
    List<Long> userIds
    
) {}
