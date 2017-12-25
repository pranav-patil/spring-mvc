package com.library.spring.scheduler.model;

public interface JobData {

    Class<? extends JobData> getJobDataClass();
}
