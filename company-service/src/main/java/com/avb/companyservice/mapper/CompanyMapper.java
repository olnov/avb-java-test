package com.avb.companyservice.mapper;

import com.avb.companyservice.dto.CompanyResponseDto;
import com.avb.companyservice.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {
    CompanyResponseDto toCompanyResponseDto(Company company);
}
