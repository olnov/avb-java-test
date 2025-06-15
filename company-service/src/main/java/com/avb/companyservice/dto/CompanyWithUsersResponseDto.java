package com.avb.companyservice.dto;

import java.util.List;

public record CompanyWithUsersResponseDto(
        Long id,
        String companyName,
        Double budget,
        List<UserDto> users
) {}
