package com.avb.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.avb.userservice.dto.CompanyDto;

import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "COMPANY-SERVICE")
public interface CompanyClient {
	@GetMapping("/api/v1/companies/{id}")
	CompanyDto getCompanyById(@PathVariable("id") Long id);

    @PutMapping("/api/v1/companies/{companyId}/employees/{userId}")
    CompanyDto updateCompanyEmployee(
        @PathVariable("companyId") Long companyId, 
        @PathVariable("userId") Long userId,
        @RequestParam("action") String action
    );
}
