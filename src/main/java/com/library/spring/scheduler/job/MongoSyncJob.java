package com.library.spring.scheduler.job;

import com.library.spring.util.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
public class MongoSyncJob implements Job {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobMap = context.getMergedJobDataMap();
        String collection = jobMap.getString("collection");
        String service = jobMap.getString("service");

        syncServiceDataToMongoCollection(service, collection);
        log.info(String.format("Syncing data from service url %s into mongodb collection name %s...", service, collection));
    }

    private void syncServiceDataToMongoCollection(String serviceUrl, String mongoCollection) throws JobExecutionException {

        try {
            HttpClient client = new HttpClient();
            String response = client.get(serviceUrl);

            if(StringUtils.isNotBlank(response)) {

                Document document;

                try {
                    document = Document.parse(response);
                }catch (Exception ex) {
                    document = new Document();
                    document.put("data", response);
                }

                mongoTemplate.insert(document, mongoCollection);
            }

        } catch (Exception ex) {
            String errorMessage = String.format("Mongo Sync Job Failed for Service %s and collection %s", serviceUrl, mongoCollection);
            throw new JobExecutionException(errorMessage, ex);
        }
    }
}
