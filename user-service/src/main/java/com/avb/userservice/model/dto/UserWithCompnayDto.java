package com.avb.userservice.model.dto;

public record UserWithCompnayDto(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        CompanyDto company
) {}
