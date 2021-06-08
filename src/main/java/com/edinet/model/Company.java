package com.edinet.model;

public class Company {
	// 会社コード
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
