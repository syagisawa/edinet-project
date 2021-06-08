package com.edinet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edinet.model.RevenueEntity;

@Repository
public interface RevenueRepository extends JpaRepository<RevenueEntity,Integer> {

	public RevenueEntity findByCompanyCode(String companyCode);
}