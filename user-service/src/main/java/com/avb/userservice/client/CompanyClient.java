package com.avb.userservice.client;

import com.avb.userservice.model.dto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", url = "http://localhost:8082")
public interface CompanyClient {
	@GetMapping("/api/v1/companies/{id}")
	CompanyDto getCompanyById(@PathVariable("id") Long id);

    @PatchMapping("/api/v1/companies/{companyId}/employees/{userId}")
    CompanyDto addCompanyEmployee(@PathVariable("companyId") Long companyId, @PathVariable("userId") Long userId);
}
