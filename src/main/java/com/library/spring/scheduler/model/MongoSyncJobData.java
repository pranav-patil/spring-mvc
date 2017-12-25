package com.library.spring.scheduler.model;

public class MongoSyncJobData implements JobData {

    private String collection;
    private String service;

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

    @Override
    public Class<? extends JobData> getJobDataClass() {
        return MongoSyncJobData.class;
    }
}
