package com.avb.companyservice.service;

import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.avb.companyservice.model.dto.CompanyResponseDto;
import com.avb.companyservice.model.dto.CreateCompanyDto;
import com.avb.companyservice.model.entity.Company;
import com.avb.companyservice.model.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyResponseDto createCompany(CreateCompanyDto companyDto) {
        Company company = new Company();
        company.setCompanyName(companyDto.companyName());
        company.setBudget(companyDto.budget());
        company.setUserIds(
                companyDto.userIds() != null ? companyDto.userIds() : Collections.emptyList());
        Company newCompany = companyRepository.save(company);
        return toCompanyResponseDto(newCompany);
    }

    public CompanyResponseDto addCompanyEmployee(Long companyId, Long userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + companyId));
        if (company.getUserIds() == null) {
            company.setUserIds(Collections.singletonList(userId));
        } else {
            company.getUserIds().add(userId);
        }
        Company updatedCompany = companyRepository.save(company);
        return toCompanyResponseDto(updatedCompany);
    }

    public CompanyResponseDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id));
        return toCompanyResponseDto(company);
    }

    public CompanyResponseDto getCompanyByName(String companyName) {
        Company company = companyRepository.findAll().stream()
                .filter(c -> c.getCompanyName().equalsIgnoreCase(companyName))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with name: " + companyName));
        return toCompanyResponseDto(company);
    }

    private CompanyResponseDto toCompanyResponseDto(Company company) {
        return new CompanyResponseDto(
                company.getId(),
                company.getCompanyName(),
                company.getBudget(),
                company.getUserIds() != null ? company.getUserIds() : Collections.emptyList()
        );
    }
}
