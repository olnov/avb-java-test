package com.avb.companyservice.model.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCompanyDto(
        @NotBlank(message = "Company name is required")
        @Size(min = 1, max = 50, message = "Company name must be between 1 and 50 characters")
        String companyName,
        Double budget,
        List<Long> userIds
) {}
