package com.library.spring.web.model;

import java.util.Date;

public class BatchTask {

    private String id;
    private String collection;
    private String service;
    private Integer refreshDuration;
    private Date lastExecutionDate;
    private String action;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getRefreshDuration() {
        return refreshDuration;
    }

    public void setRefreshDuration(Integer refreshDuration) {
        this.refreshDuration = refreshDuration;
    }

    public Date getLastExecutionDate() {
        return lastExecutionDate;
    }

    public void setLastExecutionDate(Date lastExecutionDate) {
        this.lastExecutionDate = lastExecutionDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "HOWLA";
    }
}
