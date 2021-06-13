package com.domain.models;

public class Asset {
	// 会社コード
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
