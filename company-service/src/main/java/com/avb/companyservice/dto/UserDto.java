package com.avb.companyservice.dto;

public record UserDto(
    Long id,
    String firstName,
    String lastName,
    String phoneNumber
) {}
