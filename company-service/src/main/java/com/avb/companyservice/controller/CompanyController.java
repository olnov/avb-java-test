package com.avb.companyservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import com.avb.companyservice.dto.CompanyResponseDto;
import com.avb.companyservice.dto.CompanyWithUsersResponseDto;
import com.avb.companyservice.dto.CreateCompanyDto;
import com.avb.companyservice.service.CompanyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponseDto createCompany(@RequestBody @Valid CreateCompanyDto companyDto) {
        log.debug("Creating company with name: {}", companyDto.companyName());
        return companyService.createCompany(companyDto);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<CompanyResponseDto> getAllCompanies() {
        log.info("Fetching all companies");
        var companies = companyService.getAllCompanies();
        log.debug("Fetched {} companies", companies.size());
        return companies;
    }

    @PutMapping("/{companyId}/employees/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponseDto updateCompanyEmployee(
        @PathVariable Long companyId, 
        @PathVariable Long userId, 
        @RequestParam String action
    ) {
        log.info("Updating company employee");
        log.debug("{} employee with userId: {} in company with id: {}", action, userId, companyId);
        return companyService.updateCompanyEmployee(companyId, userId, action);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyWithUsersResponseDto getCompanyById(@PathVariable Long id) {
        log.info("Retrieving company with id: {}", id);
        return companyService.getCompanyWithUsersById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponseDto updateCompany(@PathVariable Long id, CreateCompanyDto companyDto) {
        log.info("Updating company with id: {}", id);
        log.debug("Company update details: {}", companyDto);
        return companyService.updateCompanyById(id, companyDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable Long id) {
        log.info("Deleting company with id: {}", id);
        companyService.deleteCompany(id);
    }
}
