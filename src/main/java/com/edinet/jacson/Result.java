
package com.edinet.jacson;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "seqNumber",
    "docID",
    "edinetCode",
    "secCode",
    "JCN",
    "filerName",
    "fundCode",
    "ordinanceCode",
    "formCode",
    "docTypeCode",
    "periodStart",
    "periodEnd",
    "submitDateTime",
    "docDescription",
    "issuerEdinetCode",
    "subjectEdinetCode",
    "subsidiaryEdinetCode",
    "currentReportReason",
    "parentDocID",
    "opeDateTime",
    "withdrawalStatus",
    "docInfoEditStatus",
    "disclosureStatus",
    "xbrlFlag",
    "pdfFlag",
    "attachDocFlag",
    "englishDocFlag"
})
public class Result {

    @JsonProperty("seqNumber")
    private Integer seqNumber;
    @JsonProperty("docID")
    private String docID;
    @JsonProperty("edinetCode")
    private Object edinetCode;
    @JsonProperty("secCode")
    private Object secCode;
    @JsonProperty("JCN")
    private Object jCN;
    @JsonProperty("filerName")
    private Object filerName;
    @JsonProperty("fundCode")
    private Object fundCode;
    @JsonProperty("ordinanceCode")
    private Object ordinanceCode;
    @JsonProperty("formCode")
    private Object formCode;
    @JsonProperty("docTypeCode")
    private Object docTypeCode;
    @JsonProperty("periodStart")
    private Object periodStart;
    @JsonProperty("periodEnd")
    private Object periodEnd;
    @JsonProperty("submitDateTime")
    private Object submitDateTime;
    @JsonProperty("docDescription")
    private Object docDescription;
    @JsonProperty("issuerEdinetCode")
    private Object issuerEdinetCode;
    @JsonProperty("subjectEdinetCode")
    private Object subjectEdinetCode;
    @JsonProperty("subsidiaryEdinetCode")
    private Object subsidiaryEdinetCode;
    @JsonProperty("currentReportReason")
    private Object currentReportReason;
    @JsonProperty("parentDocID")
    private Object parentDocID;
    @JsonProperty("opeDateTime")
    private Object opeDateTime;
    @JsonProperty("withdrawalStatus")
    private String withdrawalStatus;
    @JsonProperty("docInfoEditStatus")
    private String docInfoEditStatus;
    @JsonProperty("disclosureStatus")
    private String disclosureStatus;
    @JsonProperty("xbrlFlag")
    private String xbrlFlag;
    @JsonProperty("pdfFlag")
    private String pdfFlag;
    @JsonProperty("attachDocFlag")
    private String attachDocFlag;
    @JsonProperty("englishDocFlag")
    private String englishDocFlag;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("seqNumber")
    public Integer getSeqNumber() {
        return seqNumber;
    }

    @JsonProperty("seqNumber")
    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    @JsonProperty("docID")
    public String getDocID() {
        return docID;
    }

    @JsonProperty("docID")
    public void setDocID(String docID) {
        this.docID = docID;
    }

    @JsonProperty("edinetCode")
    public Object getEdinetCode() {
        return edinetCode;
    }

    @JsonProperty("edinetCode")
    public void setEdinetCode(Object edinetCode) {
        this.edinetCode = edinetCode;
    }

    @JsonProperty("secCode")
    public Object getSecCode() {
        return secCode;
    }

    @JsonProperty("secCode")
    public void setSecCode(Object secCode) {
        this.secCode = secCode;
    }

    @JsonProperty("JCN")
    public Object getJCN() {
        return jCN;
    }

    @JsonProperty("JCN")
    public void setJCN(Object jCN) {
        this.jCN = jCN;
    }

    @JsonProperty("filerName")
    public Object getFilerName() {
        return filerName;
    }

    @JsonProperty("filerName")
    public void setFilerName(Object filerName) {
        this.filerName = filerName;
    }

    @JsonProperty("fundCode")
    public Object getFundCode() {
        return fundCode;
    }

    @JsonProperty("fundCode")
    public void setFundCode(Object fundCode) {
        this.fundCode = fundCode;
    }

    @JsonProperty("ordinanceCode")
    public Object getOrdinanceCode() {
        return ordinanceCode;
    }

    @JsonProperty("ordinanceCode")
    public void setOrdinanceCode(Object ordinanceCode) {
        this.ordinanceCode = ordinanceCode;
    }

    @JsonProperty("formCode")
    public Object getFormCode() {
        return formCode;
    }

    @JsonProperty("formCode")
    public void setFormCode(Object formCode) {
        this.formCode = formCode;
    }

    @JsonProperty("docTypeCode")
    public Object getDocTypeCode() {
        return docTypeCode;
    }

    @JsonProperty("docTypeCode")
    public void setDocTypeCode(Object docTypeCode) {
        this.docTypeCode = docTypeCode;
    }

    @JsonProperty("periodStart")
    public Object getPeriodStart() {
        return periodStart;
    }

    @JsonProperty("periodStart")
    public void setPeriodStart(Object periodStart) {
        this.periodStart = periodStart;
    }

    @JsonProperty("periodEnd")
    public Object getPeriodEnd() {
        return periodEnd;
    }

    @JsonProperty("periodEnd")
    public void setPeriodEnd(Object periodEnd) {
        this.periodEnd = periodEnd;
    }

    @JsonProperty("submitDateTime")
    public Object getSubmitDateTime() {
        return submitDateTime;
    }

    @JsonProperty("submitDateTime")
    public void setSubmitDateTime(Object submitDateTime) {
        this.submitDateTime = submitDateTime;
    }

    @JsonProperty("docDescription")
    public Object getDocDescription() {
        return docDescription;
    }

    @JsonProperty("docDescription")
    public void setDocDescription(Object docDescription) {
        this.docDescription = docDescription;
    }

    @JsonProperty("issuerEdinetCode")
    public Object getIssuerEdinetCode() {
        return issuerEdinetCode;
    }

    @JsonProperty("issuerEdinetCode")
    public void setIssuerEdinetCode(Object issuerEdinetCode) {
        this.issuerEdinetCode = issuerEdinetCode;
    }

    @JsonProperty("subjectEdinetCode")
    public Object getSubjectEdinetCode() {
        return subjectEdinetCode;
    }

    @JsonProperty("subjectEdinetCode")
    public void setSubjectEdinetCode(Object subjectEdinetCode) {
        this.subjectEdinetCode = subjectEdinetCode;
    }

    @JsonProperty("subsidiaryEdinetCode")
    public Object getSubsidiaryEdinetCode() {
        return subsidiaryEdinetCode;
    }

    @JsonProperty("subsidiaryEdinetCode")
    public void setSubsidiaryEdinetCode(Object subsidiaryEdinetCode) {
        this.subsidiaryEdinetCode = subsidiaryEdinetCode;
    }

    @JsonProperty("currentReportReason")
    public Object getCurrentReportReason() {
        return currentReportReason;
    }

    @JsonProperty("currentReportReason")
    public void setCurrentReportReason(Object currentReportReason) {
        this.currentReportReason = currentReportReason;
    }

    @JsonProperty("parentDocID")
    public Object getParentDocID() {
        return parentDocID;
    }

    @JsonProperty("parentDocID")
    public void setParentDocID(Object parentDocID) {
        this.parentDocID = parentDocID;
    }

    @JsonProperty("opeDateTime")
    public Object getOpeDateTime() {
        return opeDateTime;
    }

    @JsonProperty("opeDateTime")
    public void setOpeDateTime(Object opeDateTime) {
        this.opeDateTime = opeDateTime;
    }

    @JsonProperty("withdrawalStatus")
    public String getWithdrawalStatus() {
        return withdrawalStatus;
    }

    @JsonProperty("withdrawalStatus")
    public void setWithdrawalStatus(String withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }

    @JsonProperty("docInfoEditStatus")
    public String getDocInfoEditStatus() {
        return docInfoEditStatus;
    }

    @JsonProperty("docInfoEditStatus")
    public void setDocInfoEditStatus(String docInfoEditStatus) {
        this.docInfoEditStatus = docInfoEditStatus;
    }

    @JsonProperty("disclosureStatus")
    public String getDisclosureStatus() {
        return disclosureStatus;
    }

    @JsonProperty("disclosureStatus")
    public void setDisclosureStatus(String disclosureStatus) {
        this.disclosureStatus = disclosureStatus;
    }

    @JsonProperty("xbrlFlag")
    public String getXbrlFlag() {
        return xbrlFlag;
    }

    @JsonProperty("xbrlFlag")
    public void setXbrlFlag(String xbrlFlag) {
        this.xbrlFlag = xbrlFlag;
    }

    @JsonProperty("pdfFlag")
    public String getPdfFlag() {
        return pdfFlag;
    }

    @JsonProperty("pdfFlag")
    public void setPdfFlag(String pdfFlag) {
        this.pdfFlag = pdfFlag;
    }

    @JsonProperty("attachDocFlag")
    public String getAttachDocFlag() {
        return attachDocFlag;
    }

    @JsonProperty("attachDocFlag")
    public void setAttachDocFlag(String attachDocFlag) {
        this.attachDocFlag = attachDocFlag;
    }

    @JsonProperty("englishDocFlag")
    public String getEnglishDocFlag() {
        return englishDocFlag;
    }

    @JsonProperty("englishDocFlag")
    public void setEnglishDocFlag(String englishDocFlag) {
        this.englishDocFlag = englishDocFlag;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
