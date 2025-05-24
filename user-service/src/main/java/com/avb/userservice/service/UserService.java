package com.avb.userservice.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.avb.userservice.client.CompanyClient;
import com.avb.userservice.model.dto.CompanyDto;
import com.avb.userservice.model.dto.CreateUserRequestDto;
import com.avb.userservice.model.dto.UserResponseDto;
import com.avb.userservice.model.dto.UserWithCompnayDto;
import com.avb.userservice.model.entity.User;
import com.avb.userservice.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CompanyClient companyClient;

    public UserResponseDto createUser(CreateUserRequestDto userDto) {
        CompanyDto company = companyClient.getCompanyById(userDto.companyId());
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + userDto.companyId());
        }

        System.out.println("Creating user with company: " + company.companyName());

        User user = new User();
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setPhoneNumber(userDto.phoneNumber());
        user.setCompanyId(company.id());
        user = userRepository.save(user);

        System.out.println("User created with ID: " + user.getId());

        try {
            System.out.println("Calling companyClient.addCompanyEmployee(" + company.id() + ", " + user.getId() + ")");
            CompanyDto updatedCompany = companyClient.addCompanyEmployee(company.id(), user.getId());
            System.out.println("Company employee added OK");
            if (updatedCompany == null) {
                throw new RuntimeException("Failed to add user to company");
            }
        } catch (Exception e) {
            e.printStackTrace();
            userRepository.delete(user);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding user to company: " + e.getMessage(), e);
        }
        
        return toResponseDto(user);
    }

    public UserWithCompnayDto getByFullName(String firstName, String lastName) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("User not found with name: " + firstName + " " + lastName));
        
        CompanyDto company = companyClient.getCompanyById(user.getCompanyId());

        return new UserWithCompnayDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                company
        );
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
        return toResponseDto(user);
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhoneNumber(),
            user.getCompanyId()
        );
    }
}