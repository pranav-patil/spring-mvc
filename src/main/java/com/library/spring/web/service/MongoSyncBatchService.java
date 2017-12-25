package com.library.spring.web.service;
import java.util.List;

import com.library.spring.web.model.BatchTask;

public interface MongoSyncBatchService {

	List<BatchTask> getAllMongoSyncJobs() throws Exception;
	void addMongoSyncJob(BatchTask batchTask) throws Exception;
	void updateMongoSyncJob(BatchTask batchTask) throws Exception;
	void deleteMongoSyncJob(BatchTask batchTask) throws Exception;
}
