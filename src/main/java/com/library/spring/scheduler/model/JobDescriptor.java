package com.library.spring.scheduler.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.*;

import java.util.*;

import static org.quartz.JobBuilder.newJob;

public class JobDescriptor {

    private Class<? extends Job> jobClass;
    private JobData jobData;
    private String name;
    private String group;
    private List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }

    public JobData getJobData() {
        return jobData;
    }

    public void setJobData(JobData jobData) {
        this.jobData = jobData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<TriggerDescriptor> getTriggerDescriptors() {
        return triggerDescriptors;
    }

    public void setTriggerDescriptors(List<TriggerDescriptor> triggerDescriptors) {
        this.triggerDescriptors = triggerDescriptors;
    }

    public Set<Trigger> buildTriggers() {
        Set<Trigger> triggers = new LinkedHashSet<>();
        for (TriggerDescriptor triggerDescriptor : triggerDescriptors) {
            triggers.add(triggerDescriptor.buildTrigger());
        }

        return triggers;
    }

    /**
     * Convenience method that builds a JobDetail
     *
     * @return the JobDetail built from this descriptor
     */
    public JobDetail buildJobDetail() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.convertValue(jobData, Map.class);
        JobDataMap jobDataMap = new JobDataMap(map);
        return newJob(jobClass)
                .withIdentity(getName(), getGroup())
                .usingJobData(jobDataMap)
                .build();
    }

    public JobDetail updateJobDetail(JobDetail oldJobDetail) {
        JobDataMap jobDataMap = oldJobDetail.getJobDataMap();
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.convertValue(jobData, Map.class);
        jobDataMap.putAll(map);
        JobBuilder jobBuilder = oldJobDetail.getJobBuilder();
        return jobBuilder.usingJobData(jobDataMap).storeDurably().build();
    }

    /**
     * Convenience method that builds a descriptor from JobDetail and Trigger(s)
     *
     * @param jobDetail
     *            the JobDetail instance
     * @param triggersOfJob
     *            the Trigger(s) to associate with the Job
     * @return the JobDescriptor
     */
    @SuppressWarnings("unchecked")
    public static JobDescriptor buildDescriptor(JobDetail jobDetail, List<? extends Trigger> triggersOfJob) {
        List<TriggerDescriptor> triggerDescriptors = new ArrayList<>();

        for (Trigger trigger : triggersOfJob) {
            triggerDescriptors.add(new TriggerDescriptor(trigger));
        }

        JobDescriptor jobDescriptor = new JobDescriptor();
        jobDescriptor.setName(jobDetail.getKey().getName());
        jobDescriptor.setGroup(jobDetail.getKey().getGroup());
        jobDescriptor.setJobClass(jobDetail.getJobClass());
        Map<String, Object> jobDataMap = jobDetail.getJobDataMap().getWrappedMap();

        if(!jobDataMap.isEmpty() && jobDataMap.containsKey("jobDataClass")) {
            ObjectMapper mapper = new ObjectMapper();
            Object jobDataClassObject = jobDataMap.get("jobDataClass");

            if(! (jobDataClassObject instanceof Class)) {
                try {
                    jobDataClassObject = Class.forName(jobDataClassObject.toString());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            Class<? extends JobData> jobDataClass = (Class<? extends JobData>) jobDataClassObject;
            JobData jobData = mapper.convertValue(jobDataMap, jobDataClass);
            jobDescriptor.setJobData(jobData);
        }

        jobDescriptor.setTriggerDescriptors(triggerDescriptors);
        return jobDescriptor;
    }
}
