package com.edinet.domain.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edinet.domain.models.AssetEntity;
import com.edinet.domain.repositories.AssetRepository;

@Service
@Transactional
public class AssetService {

	@Autowired
	private AssetRepository assetRepository;

	public List<AssetEntity> assetList(){
		return assetRepository.findAll();
	}

	public void insertAsset(AssetEntity entity) {
		assetRepository.save(entity);
	}

	public AssetEntity getAssetByCompanyCode(String companyCode) {
		AssetEntity entity = assetRepository.findByCompanyCode(companyCode);
		return entity;
	}

}