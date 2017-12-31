package com.library.spring.web.model;

import java.io.Serializable;

public class ResponseBean implements Serializable {

    private String id;
    private String message;
    private String responseCode;

    public ResponseBean() {
    }

    public ResponseBean(String message, String responseCode) {
        this.message = message;
        this.responseCode = responseCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
