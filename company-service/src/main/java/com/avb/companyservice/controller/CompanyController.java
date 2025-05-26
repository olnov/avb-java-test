package com.avb.companyservice.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avb.companyservice.model.dto.CompanyResponseDto;
import com.avb.companyservice.model.dto.CompanyWithUsersResponseDto;
import com.avb.companyservice.model.dto.CreateCompanyDto;
import com.avb.companyservice.service.CompanyService;

import feign.Body;
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

    @PutMapping("/{companyId}/employees/{userId}")
    public CompanyResponseDto updateCompanyEmployee(
        @PathVariable Long companyId, 
        @PathVariable Long userId, 
        @RequestParam String action
    ) {
        return companyService.udpateCompanyEmployee(companyId, userId, action);
    }

    @GetMapping("/{id}")
    public CompanyWithUsersResponseDto getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyWithUsersById(id);
    }

    @PatchMapping("/{id}")
    public CompanyResponseDto updateCompany(@PathVariable Long id, CreateCompanyDto companyDto) {
        return companyService.updateCompanyById(id, companyDto);
    }

    @DeleteMapping("/{id}")
    public String deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return "Company with id " + id + " deleted successfully.";
    }
}
