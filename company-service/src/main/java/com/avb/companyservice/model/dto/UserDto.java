package com.avb.companyservice.model.dto;

public record UserDto(
    Long id,
    String firstName,
    String lastName,
    String phoneNumber
) {}
