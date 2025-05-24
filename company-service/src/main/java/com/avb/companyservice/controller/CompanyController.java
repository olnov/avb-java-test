package com.avb.companyservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avb.companyservice.model.dto.CompanyResponseDto;
import com.avb.companyservice.model.dto.CreateCompanyDto;
import com.avb.companyservice.service.CompanyService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/")
    public CompanyResponseDto createCompany(CreateCompanyDto companyDto) {
        return companyService.createCompany(companyDto);
    }

    @PatchMapping("/{companyId}/employees/{userId}")
    public CompanyResponseDto addCompanyEmployee(@PathVariable Long companyId, @PathVariable Long userId) {
        return companyService.addCompanyEmployee(companyId, userId);
    }

    @GetMapping("/{id}")
    public CompanyResponseDto getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    @GetMapping("/name/{companyName}")
    public CompanyResponseDto getCompanyByName(@PathVariable String companyName) {
        return companyService.getCompanyByName(companyName);
    }
}
