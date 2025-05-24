package com.avb.userservice.model.dto;

public record UserResponseDto(
    Long id,
    String firstName,
    String lastName,
    String phoneNumber,
    Long companyId
) {}
