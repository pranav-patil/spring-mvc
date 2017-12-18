package com.library.spring.web.service;

import java.util.List;

import com.library.spring.web.domain.ScheduleTask;
import com.library.spring.web.mapper.ScheduleTaskMapper;
import com.library.spring.web.model.BatchTask;
import com.library.spring.web.repository.ScheduleTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class BatchTaskServiceImpl implements BatchTaskService {

	@Resource
	private ScheduleTaskRepository scheduleTaskRepository;
	@Autowired
	private ScheduleTaskMapper scheduleTaskMapper;

	public List<BatchTask> getAllBatchTasks() {
		return scheduleTaskMapper.mapToBatchTasks(scheduleTaskRepository.findAll());
	}

	public List<BatchTask> getBatchTasksByCollection(String name) {
		List<ScheduleTask> scheduleTasks = scheduleTaskRepository.findByCollection(name);
		return scheduleTaskMapper.mapToBatchTasks(scheduleTasks);
	}

	public List<BatchTask> getBatchTasksByCollectionAndService(String collection, String service) {
		List<ScheduleTask> scheduleTasks = scheduleTaskRepository.findByCollectionAndService(collection, service);
		return scheduleTaskMapper.mapToBatchTasks(scheduleTasks);
	}

	public void saveBatchTask(BatchTask batchTask) {
		ScheduleTask scheduleTask = scheduleTaskMapper.mapToScheduleTask(batchTask);
		scheduleTaskRepository.save(scheduleTask);
	}

/*
	public ScheduleTask findOne(long id) {
		return scheduleTaskRepository.findOne(id);
	}

	public void delete(long id) {
		scheduleTaskRepository.delete(id);
	}*/
}
