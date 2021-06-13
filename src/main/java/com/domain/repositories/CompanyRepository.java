package com.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.models.CompanyEntity;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity,Integer> {

	public CompanyEntity findByCompanyCode(String companyCode);
}