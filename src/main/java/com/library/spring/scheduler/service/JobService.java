package com.library.spring.scheduler.service;

import com.library.spring.scheduler.model.JobDescriptor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.quartz.JobKey.jobKey;

@Service
public class JobService {

    @Autowired
    private Scheduler scheduler;

    private final Logger log = LoggerFactory.getLogger(JobService.class);

    @Transactional(readOnly = true)
    public List<JobDescriptor> getAllJobs() throws SchedulerException {
        List<JobDescriptor> jobs = new ArrayList<>();
        for (String groupName : scheduler.getJobGroupNames()) {
            jobs.addAll(getJobs(groupName));
        }
        return jobs;
    }

    @Transactional(readOnly = true)
    public List<JobDescriptor> getJobs(String group) throws SchedulerException {

        List<JobDescriptor> jobs = new ArrayList<>();
        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            jobs.add(JobDescriptor.buildDescriptor(jobDetail, triggers));
        }
        return jobs;
    }

    @Transactional(readOnly = true)
    public Optional<JobDescriptor> findJob(String group, String name) throws SchedulerException {
        JobDetail jobDetail = scheduler.getJobDetail(jobKey(name, group));
        if(Objects.nonNull(jobDetail)) {
            return Optional.of(JobDescriptor.buildDescriptor(jobDetail, scheduler.getTriggersOfJob(jobKey(name, group))));
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<? extends Trigger> getTriggers(String group, String name) throws SchedulerException {
        return scheduler.getTriggersOfJob(jobKey(name, group));
    }

    public JobDescriptor createJob(String group, JobDescriptor descriptor) throws SchedulerException {
        descriptor.setGroup(group);
        JobDetail jobDetail = descriptor.buildJobDetail();
        Set<Trigger> triggersForJob = descriptor.buildTriggers();
        scheduler.scheduleJob(jobDetail, triggersForJob, false);
        return descriptor;
    }

    public void updateJob(String group, String name, JobDescriptor descriptor) throws SchedulerException {
        JobDetail oldJobDetail = scheduler.getJobDetail(jobKey(name, group));
        if(Objects.nonNull(oldJobDetail)) {
            JobDetail newJobDetail = descriptor.updateJobDetail(oldJobDetail);
            scheduler.addJob(newJobDetail, true);
            return;
        }
    }

    public void deleteJob(String group, String name) throws SchedulerException {
        scheduler.deleteJob(jobKey(name, group));
    }

    public void pauseJob(String group, String name) throws SchedulerException {
        scheduler.pauseJob(jobKey(name, group));
    }

    public void resumeJob(String group, String name) throws SchedulerException {
        scheduler.resumeJob(jobKey(name, group));
    }
}
