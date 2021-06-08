package com.edinet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edinet.model.AssetEntity;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity,Integer> {

	public AssetEntity findByCompanyCode(String companyCode);

}