package com.domain.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domain.models.RevenueEntity;
import com.domain.repositories.RevenueRepository;

@Service
@Transactional
public class RevenueService {

	@Autowired
	private RevenueRepository RevenueRepository;

	public List<RevenueEntity> RevenueList(){
		return RevenueRepository.findAll();
	}

	public void insertRevenue(RevenueEntity entity) {
		RevenueRepository.save(entity);
	}

	public RevenueEntity getRevenueByCompanyCode(String companyCode) {
		RevenueEntity entity = RevenueRepository.findByCompanyCode(companyCode);
		return entity;
	}

}