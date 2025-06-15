package com.avb.userservice.service;

import java.util.List;
import java.util.stream.Collectors;

import com.avb.userservice.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.avb.userservice.client.CompanyClient;
import com.avb.userservice.dto.CompanyDto;
import com.avb.userservice.dto.CreateUserRequestDto;
import com.avb.userservice.dto.UpdateUserRequestDto;
import com.avb.userservice.dto.UserResponseDto;
import com.avb.userservice.dto.UserWithCompnayDto;
import com.avb.userservice.entity.User;
import com.avb.userservice.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final CompanyClient companyClient;
    private final UserMapper userMapper;

    public UserResponseDto createUser(CreateUserRequestDto userDto) {
        CompanyDto company = companyClient.getCompanyById(userDto.companyId());
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + userDto.companyId());
        }

        User user = new User();
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setPhoneNumber(userDto.phoneNumber());
        user.setCompanyId(company.id());
        user = userRepository.save(user);

        try {
            CompanyDto updatedCompany = companyClient.updateCompanyEmployee(company.id(), user.getId(), "add");
            System.out.println("Company employee added OK");
            if (updatedCompany == null) {
                throw new RuntimeException("Failed to add user to company");
            }
        } catch (Exception e) {
            e.printStackTrace();
            userRepository.delete(user);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding user to company: " + e.getMessage(), e);
        }
        
        log.info("User created with id: {}", user.getId());
        log.debug("User details: {}", user);
        return userMapper.toResponseDto(user);
    }

    public UserWithCompnayDto getByFullName(String firstName, String lastName) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("User not found with name: " + firstName + " " + lastName));
        
        CompanyDto company = companyClient.getCompanyById(user.getCompanyId());

        UserWithCompnayDto userWithCompany = new UserWithCompnayDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                company
        );

        log.info("User retrieved with id: {}", user.getId());
        log.debug("User details: {}", userWithCompany);
        return userWithCompany;
    }

    public List<UserWithCompnayDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    CompanyDto company = companyClient.getCompanyById(user.getCompanyId());
                    return new UserWithCompnayDto(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getPhoneNumber(),
                            company
                    );
                })
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with id: " + id));
        
        log.info("User retrieved with id: {}", user.getId());
        log.debug("User details: {}", user);
        return userMapper.toResponseDto(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        
        try {
            CompanyDto updatedCompany = companyClient.updateCompanyEmployee(user.getCompanyId(), user.getId(), "remove");
            if (updatedCompany == null) {
                throw new RuntimeException("Failed to remove user from company");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error removing user from company: " + e.getMessage(), e);
        }

        log.info("Deleting user with id: {}", user.getId());
        log.debug("User details: {}", user);
        userRepository.delete(user);
    }

    public UserResponseDto updateUser(Long id, UpdateUserRequestDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));

        CompanyDto company = companyClient.getCompanyById(userDto.companyId());
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + userDto.companyId());
        }

        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setPhoneNumber(userDto.phoneNumber());
        user.setCompanyId(company.id());

        user = userRepository.save(user);

        log.info("User updated with id: {}", user.getId());
        log.debug("User details: {}", user);
        return userMapper.toResponseDto(user);
    }
}