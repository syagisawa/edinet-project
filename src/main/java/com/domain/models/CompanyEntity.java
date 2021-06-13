package com.domain.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class CompanyEntity {

	// 会社コード
	@Id
	private String companyCode;
	// 会社名
	private String companyName;
	// 期開始日
	private String startPeriodDate;

	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getStartPeriodDate() {
		return startPeriodDate;
	}
	public void setStartPeriodDate(String startPeriodDate) {
		this.startPeriodDate = startPeriodDate;
	}

}