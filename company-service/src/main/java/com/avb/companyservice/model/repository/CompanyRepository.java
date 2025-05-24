package com.avb.companyservice.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avb.companyservice.model.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    
}
