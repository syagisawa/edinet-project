
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
    "title",
    "parameter",
    "resultset",
    "processDateTime",
    "status",
    "message"
})
public class Metadata {

    @JsonProperty("title")
    private String title;
    @JsonProperty("parameter")
    private Parameter parameter;
    @JsonProperty("resultset")
    private Resultset resultset;
    @JsonProperty("processDateTime")
    private String processDateTime;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("parameter")
    public Parameter getParameter() {
        return parameter;
    }

    @JsonProperty("parameter")
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    @JsonProperty("resultset")
    public Resultset getResultset() {
        return resultset;
    }

    @JsonProperty("resultset")
    public void setResultset(Resultset resultset) {
        this.resultset = resultset;
    }

    @JsonProperty("processDateTime")
    public String getProcessDateTime() {
        return processDateTime;
    }

    @JsonProperty("processDateTime")
    public void setProcessDateTime(String processDateTime) {
        this.processDateTime = processDateTime;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
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
