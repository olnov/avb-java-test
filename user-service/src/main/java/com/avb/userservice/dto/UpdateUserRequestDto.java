package com.avb.userservice.dto;

import com.avb.userservice.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,

        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,

        @ValidPhoneNumber
        String phoneNumber,

        Long companyId
) {}
