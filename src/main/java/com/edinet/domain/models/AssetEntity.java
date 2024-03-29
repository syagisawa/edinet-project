package com.edinet.domain.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "asset")
public class AssetEntity {
	// 会社コード
	@Id
	private String companyCode;
	// 期開始日
	private String startPeriodDate;
	// 自己資本比率
	private String capitalAdequacyRatio;

	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getStartPeriodDate() {
		return startPeriodDate;
	}
	public void setStartPeriodDate(String startPeriodDate) {
		this.startPeriodDate = startPeriodDate;
	}
	public String getCapitalAdequacyRatio() {
		return capitalAdequacyRatio;
	}
	public void setCapitalAdequacyRatio(String capitalAdequacyRatio) {
		this.capitalAdequacyRatio = capitalAdequacyRatio;
	}
}