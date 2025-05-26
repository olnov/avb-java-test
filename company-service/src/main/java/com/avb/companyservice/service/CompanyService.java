package com.avb.companyservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.avb.companyservice.client.UserClient;
import com.avb.companyservice.model.dto.CompanyResponseDto;
import com.avb.companyservice.model.dto.CompanyWithUsersResponseDto;
import com.avb.companyservice.model.dto.CreateCompanyDto;
import com.avb.companyservice.model.dto.UserDto;
import com.avb.companyservice.model.entity.Company;
import com.avb.companyservice.model.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserClient userClient;

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

    public CompanyResponseDto udpateCompanyEmployee(Long companyId, Long userId, String action) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + companyId));
        
        List<Long> userIds = company.getUserIds() !=null
                ? new ArrayList<>(company.getUserIds())
                : new ArrayList<>();

        switch(action) {
            case "add":
                if (!userIds.contains(userId)) {
                    userIds.add(userId);
                }
                break;
            case "remove":
                userIds.remove(userId);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action: " + action);
        }
        company.setUserIds(userIds);
        Company updatedCompany = companyRepository.save(company);
        return toCompanyResponseDto(updatedCompany);
    }

    
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id));
        companyRepository.delete(company);
    }

    public CompanyWithUsersResponseDto getCompanyWithUsersById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id));
        
        List<UserDto> users = company.getUserIds() != null ? 
                company.getUserIds().stream()
                        .map(userId -> userClient.getUserById(userId))
                        .filter(user -> user != null)
                        .collect(Collectors.toList()) 
                : Collections.emptyList();

        return new CompanyWithUsersResponseDto(
                company.getId(),
                company.getCompanyName(),
                company.getBudget(),
                users
        );
    }

    public CompanyResponseDto updateCompanyById(Long id, CreateCompanyDto companyDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id));
        
        company.setCompanyName(companyDto.companyName());
        company.setBudget(companyDto.budget());
        company.setUserIds(companyDto.userIds() != null ? companyDto.userIds() : Collections.emptyList());
        
        Company updatedCompany = companyRepository.save(company);
        return toCompanyResponseDto(updatedCompany);
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
