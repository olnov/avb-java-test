package com.avb.companyservice.model.dto;

import java.util.List;

public record CompanyWithUsersResponseDto(
        Long id,
        String companyName,
        Double budget,
        List<UserDto> users
) {}
