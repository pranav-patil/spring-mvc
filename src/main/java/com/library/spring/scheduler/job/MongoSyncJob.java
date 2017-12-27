package com.library.spring.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class MongoSyncJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobMap = context.getMergedJobDataMap();
        String collection = jobMap.getString("collection");
        String service = jobMap.getString("service");

        log.info(String.format("Syncing data from service url %s into mongodb collection name %s...", service, collection));
    }
}
