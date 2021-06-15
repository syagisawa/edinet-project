package com.edinet.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edinet.domain.models.RevenueEntity;

@Repository
public interface RevenueRepository extends JpaRepository<RevenueEntity,Integer> {

	public RevenueEntity findByCompanyCode(String companyCode);
}