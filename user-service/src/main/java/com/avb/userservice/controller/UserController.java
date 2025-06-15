package com.avb.userservice.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.avb.userservice.dto.CreateUserRequestDto;
import com.avb.userservice.dto.UpdateUserRequestDto;
import com.avb.userservice.dto.UserResponseDto;
import com.avb.userservice.dto.UserWithCompnayDto;
import com.avb.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid CreateUserRequestDto userDto) {
        log.debug("Creating user with name: {} {}", userDto.firstName(), userDto.lastName());
        return userService.createUser(userDto);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserWithCompnayDto> getUsers() {
        log.info("Fetching all users");
        List<UserWithCompnayDto> users = userService.getAllUsers();
        log.debug("Fetched {} users", users.size());
        return users;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserById(@PathVariable @Valid Long id) {
        log.info("Retrieving user with id: {}", id);
        if (id <= 0) {
            log.warn("Invalid user ID: {}", id);
            throw new IllegalArgumentException("Invalid ID provided. ID must be a positive number.");
        }
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequestDto userDto) {
        if (id <= 0) {
            log.warn("Invalid user ID: {}", id);
            throw new IllegalArgumentException("Invalid ID provided. ID must be a positive number.");
        }
        log.info("Updating user with id: {}", id);
        log.debug("User update details: {}", userDto);
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        userService.deleteUser(id);
    }
}
