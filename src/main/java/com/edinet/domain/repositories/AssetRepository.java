package com.edinet.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edinet.domain.models.AssetEntity;


@Repository
public interface AssetRepository extends JpaRepository<AssetEntity,Integer> {

	public AssetEntity findByCompanyCode(String companyCode);

}