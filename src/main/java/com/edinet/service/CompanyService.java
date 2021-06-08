package com.edinet.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edinet.model.CompanyEntity;
import com.edinet.repository.CompanyRepository;

@Service
@Transactional
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	public List<CompanyEntity> getCompanyList(){
		return companyRepository.findAll();
	}

	public void insertCmpany(CompanyEntity entity) {
		companyRepository.save(entity);
	}

	public CompanyEntity getCompanyDataByCompanyCode(String companyCode) {
		CompanyEntity entity = companyRepository.findByCompanyCode(companyCode);
		return entity;
	}

}