package com.edinet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edinet.model.CompanyEntity;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity,Integer> {

	public CompanyEntity findByCompanyCode(String companyCode);
}