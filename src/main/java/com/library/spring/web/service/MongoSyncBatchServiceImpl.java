package com.library.spring.web.service;

import com.library.spring.scheduler.job.MongoSyncJob;
import com.library.spring.scheduler.model.JobDescriptor;
import com.library.spring.scheduler.model.MongoSyncJobData;
import com.library.spring.scheduler.model.TriggerDescriptor;
import com.library.spring.scheduler.service.JobService;
import com.library.spring.web.model.BatchTask;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MongoSyncBatchServiceImpl implements MongoSyncBatchService {

	@Autowired
	private JobService jobService;

	private static final String MONGO_SYNC_JOB_GROUP = "mongosync";

	public List<BatchTask> getAllMongoSyncJobs() throws Exception {

		List<BatchTask> batchTasks = new ArrayList<>();

		for (JobDescriptor job : jobService.getJobs(MONGO_SYNC_JOB_GROUP)) {
			if(job.getJobData() instanceof MongoSyncJobData) {
				MongoSyncJobData jobData = (MongoSyncJobData) job.getJobData();
				BatchTask batchTask = new BatchTask();
				batchTask.setId(job.getName());
				batchTask.setCollection(jobData.getCollection());
				batchTask.setService(jobData.getService());

				List<? extends Trigger> triggers = jobService.getTriggers(MONGO_SYNC_JOB_GROUP, job.getName());
				batchTask.setLastExecutionDate(TriggerDescriptor.getLastExecutionDate(triggers));

				Date futureExecutionDate = TriggerDescriptor.getFutureExecutionDate(triggers);
				LocalDateTime futureExecutionDateTime = LocalDateTime.ofInstant(futureExecutionDate.toInstant(), ZoneId.systemDefault());
				long minutes = Duration.between(futureExecutionDateTime, LocalDateTime.now()).toMinutes();
				batchTask.setRefreshDuration((int) minutes);

				batchTasks.add(batchTask);
			}
		}
		return batchTasks;
	}

	public void addMongoSyncJob(BatchTask batchTask) throws Exception {
		JobDescriptor jobDescriptor = mapToJobDescriptor(batchTask);
		jobService.createJob(MONGO_SYNC_JOB_GROUP, jobDescriptor);
//		ScheduleTask scheduleTask = scheduleTaskMapper.mapToScheduleTask(batchTask);
//		scheduleTaskRepository.save(scheduleTask);
	}

	public void updateMongoSyncJob(BatchTask batchTask) throws Exception {
		JobDescriptor jobDescriptor = mapToJobDescriptor(batchTask);
		jobService.updateJob(MONGO_SYNC_JOB_GROUP, batchTask.getId(), jobDescriptor);
	}

	public void deleteMongoSyncJob(BatchTask batchTask) throws Exception {
		jobService.deleteJob(MONGO_SYNC_JOB_GROUP, batchTask.getId());
	}

	private JobDescriptor mapToJobDescriptor(BatchTask batchTask) {
		JobDescriptor jobDescriptor = new JobDescriptor();
		jobDescriptor.setName(batchTask.getId());
		jobDescriptor.setGroup(MONGO_SYNC_JOB_GROUP);
		jobDescriptor.setJobClass(MongoSyncJob.class);
		MongoSyncJobData mongoSyncJobData = new MongoSyncJobData();
		mongoSyncJobData.setCollection(batchTask.getCollection());
		mongoSyncJobData.setService(batchTask.getService());
		jobDescriptor.setJobData(mongoSyncJobData);

		List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();
		TriggerDescriptor triggerDescriptor = new TriggerDescriptor();
		triggerDescriptor.setName(String.format("%s_TGR", batchTask.getId()));
		triggerDescriptor.setGroup(MONGO_SYNC_JOB_GROUP);
		triggerDescriptor.setCron(String.format("0 */%s * ? * *", batchTask.getRefreshDuration()));
		triggerDescriptors.add(triggerDescriptor);
		jobDescriptor.setTriggerDescriptors(triggerDescriptors);
		return jobDescriptor;
	}
}
