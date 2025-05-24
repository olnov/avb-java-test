package com.avb.companyservice.model.dto;

import java.util.List;

public record CompanyResponseDto(
        Long id,
        String companyName,
        Double budget,
        List<Long> userIds
) {}
