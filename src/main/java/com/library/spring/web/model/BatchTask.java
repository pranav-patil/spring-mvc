package com.library.spring.web.model;

import java.util.Date;

public class BatchTask {

    private String collectionName;
    private String service;
    private Integer refreshDuration;
    private Date lastExecutionDate;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
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

    @Override
    public String toString() {
        return "HOWLA";
    }
}
