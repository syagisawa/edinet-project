package com.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.domain.models.RevenueEntity;

@Repository
public interface RevenueRepository extends JpaRepository<RevenueEntity,Integer> {

	public RevenueEntity findByCompanyCode(String companyCode);
}