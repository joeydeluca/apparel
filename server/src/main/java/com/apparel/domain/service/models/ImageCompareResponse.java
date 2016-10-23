package com.apparel.domain.service.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Joe Deluca on 10/23/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageCompareResponse {
    private Float result;
    private Boolean success;

    public Float getResult() {
        return result;
    }

    public void setResult(Float result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
