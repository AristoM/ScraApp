package com.scraapp.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AbstractApiResponse implements Serializable {

    private String requestTag;

    public String getRequestTag() {
        return requestTag;
    }

    public void setRequestTag(String requestTag) {
        this.requestTag = requestTag;
    }

    public String message;

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
