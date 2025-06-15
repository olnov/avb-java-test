package com.avb.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.avb.userservice.dto.UserResponseDto;
import com.avb.userservice.entity.User;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponseDto toResponseDto(User user);
}
