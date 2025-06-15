package com.avb.companyservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.avb.companyservice.mapper.CompanyMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.avb.companyservice.client.UserClient;
import com.avb.companyservice.dto.CompanyResponseDto;
import com.avb.companyservice.dto.CompanyWithUsersResponseDto;
import com.avb.companyservice.dto.CreateCompanyDto;
import com.avb.companyservice.dto.UserDto;
import com.avb.companyservice.entity.Company;
import com.avb.companyservice.repository.CompanyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserClient userClient;
    private final CompanyMapper companyMapper;

    public CompanyResponseDto createCompany(CreateCompanyDto companyDto) {
        Company company = new Company();
        company.setCompanyName(companyDto.companyName());
        company.setBudget(companyDto.budget());
        company.setUserIds(
                companyDto.userIds() != null ? companyDto.userIds() : Collections.emptyList());
        Company newCompany = companyRepository.save(company);
        log.info("Company created with id: {}", newCompany.getId());
        log.debug("Company details: {}", newCompany);
        return companyMapper.toCompanyResponseDto(newCompany);
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
        log.info("Added user with id: {} to company with id: {}", userId, companyId);
        log.debug("Updated company details: {}", updatedCompany);
        return companyMapper.toCompanyResponseDto(updatedCompany);
    }

    public List<CompanyResponseDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        log.debug("Fetched {} companies", companies.size());
        return companies.stream()
                .map(companyMapper::toCompanyResponseDto)
                .collect(Collectors.toList());
    }

    public CompanyResponseDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id));
        log.info("Retrieved company with id: {}", id);
        log.debug("Company details: {}", company);
        return companyMapper.toCompanyResponseDto(company);
    }

    public CompanyResponseDto updateCompanyEmployee(Long companyId, Long userId, String action) {
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
        log.info("Updated company with id: {} by {} user with id: {}", companyId, action, userId);
        log.debug("Updated company details: {}", updatedCompany);
        return companyMapper.toCompanyResponseDto(updatedCompany);
    }

    
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id));
        log.info("Deleting company with id: {}", id);
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
        
        var companyWithUsers = new CompanyWithUsersResponseDto(
                company.getId(),
                company.getCompanyName(),
                company.getBudget(),
                users
        );

        log.info("Retrieved company with id: {} and its users", id);
        log.debug("Company with users details: {}", companyWithUsers);
        return companyWithUsers;
    }

    public CompanyResponseDto updateCompanyById(Long id, CreateCompanyDto companyDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id));
        
        company.setCompanyName(companyDto.companyName());
        company.setBudget(companyDto.budget());
        company.setUserIds(companyDto.userIds() != null ? companyDto.userIds() : Collections.emptyList());
        
        Company updatedCompany = companyRepository.save(company);
        log.info("Updated company with id: {}", id);
        log.debug("Updated company details: {}", updatedCompany);
        return companyMapper.toCompanyResponseDto(updatedCompany);
    }

//    private CompanyResponseDto toCompanyResponseDto(Company company) {
//        return new CompanyResponseDto(
//                company.getId(),
//                company.getCompanyName(),
//                company.getBudget(),
//                company.getUserIds() != null ? company.getUserIds() : Collections.emptyList()
//        );
//    }
}
