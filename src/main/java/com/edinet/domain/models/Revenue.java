package com.edinet.domain.models;

public class Revenue {
	// 会社コード
	private String companyCode;
	// 期開始日
	private String startPeriodDate;
	// 売上高
	private String netSales;
	// 営業利益
	private String 	operatingRevenue;
	// 経常利益
	private String ordinaryIncome;

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
	public String getNetSales() {
		return netSales;
	}
	public void setNetSales(String netSales) {
		this.netSales = netSales;
	}
	public String getOperatingRevenue() {
		return operatingRevenue;
	}
	public void setOperatingRevenue(String operatingRevenue) {
		this.operatingRevenue = operatingRevenue;
	}
	public String getOrdinaryIncome() {
		return ordinaryIncome;
	}
	public void setOrdinaryIncome(String ordinaryIncome) {
		this.ordinaryIncome = ordinaryIncome;
	}
}