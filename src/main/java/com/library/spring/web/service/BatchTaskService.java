package com.library.spring.web.service;
import java.util.List;

import com.library.spring.web.model.BatchTask;

public interface BatchTaskService {

	List<BatchTask> getAllBatchTasks();
	void saveBatchTask(BatchTask batchTask);
	List<BatchTask> getBatchTasksByCollection(String collection);
	List<BatchTask> getBatchTasksByCollectionAndService(String collection, String service);
}
