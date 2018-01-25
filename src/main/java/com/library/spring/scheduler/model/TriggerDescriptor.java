package com.library.spring.scheduler.model;

import org.quartz.JobDataMap;
import org.quartz.Trigger;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static java.time.ZoneId.systemDefault;
import static java.util.UUID.randomUUID;
import static org.quartz.CronExpression.isValidExpression;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.springframework.util.StringUtils.isEmpty;

public class TriggerDescriptor {

    private String name;
    private String group;
    private LocalDateTime fireTime;
    private String cron;
    private Integer minuteInterval;

    public TriggerDescriptor() {
    }

    /**
     * @param trigger
     *            the Trigger used to build this descriptor
     */
    public TriggerDescriptor(Trigger trigger) {
        this.name = trigger.getKey().getName();
        this.group = trigger.getKey().getGroup();
        this.cron = trigger.getJobDataMap().getString("cron");
        this.minuteInterval = getInteger(trigger.getJobDataMap().get("minuteInterval"));
        this.fireTime = (LocalDateTime) trigger.getJobDataMap().get("fireTime");
    }

    public TriggerDescriptor setName(final String name) {
        this.name = name;
        return this;
    }

    public TriggerDescriptor setGroup(final String group) {
        this.group = group;
        return this;
    }

    public TriggerDescriptor setFireTime(final LocalDateTime fireTime) {
        this.fireTime = fireTime;
        return this;
    }

    public TriggerDescriptor setCron(final String cron) {
        this.cron = cron;
        return this;
    }

    public TriggerDescriptor setMinuteInterval(final Integer minuteInterval) {
        this.minuteInterval = minuteInterval;
        return this;
    }

    private String buildName() {
        return isEmpty(name) ? randomUUID().toString() : name;
    }

    /**
     * Convenience method for building a Trigger
     *
     * @return the Trigger associated with this descriptor
     */
    public Trigger buildTrigger() {

        if (!isEmpty(cron)) {

            if (!isValidExpression(cron)) {
                throw new IllegalArgumentException("Provided expression " + cron + " is not a valid cron expression");
            }

            return newTrigger()
                    .withIdentity(buildName(), group)
                    .withSchedule(cronSchedule(cron)
                            .withMisfireHandlingInstructionFireAndProceed()
                            .inTimeZone(TimeZone.getTimeZone(systemDefault())))
                    .usingJobData("cron", cron)
                    .build();

        } else if (!isEmpty(minuteInterval)) {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("minuteInterval", minuteInterval);
            return newTrigger()
                    .withIdentity(buildName(), group)
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(minuteInterval).repeatForever())
                    .usingJobData(jobDataMap)
                    .build();
        } else if (!isEmpty(fireTime)) {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("fireTime", fireTime);
            return newTrigger()
                    .withIdentity(buildName(), group)
                    .withSchedule(simpleSchedule()
                            .withMisfireHandlingInstructionNextWithExistingCount())
                    .startAt(java.sql.Date.from(fireTime.atZone(systemDefault()).toInstant()))
                    .usingJobData(jobDataMap)
                    .build();
        }

        throw new IllegalStateException("unsupported trigger descriptor " + this);
    }
    
    public static Date getLastExecutionDate(List<? extends Trigger> triggers) {
        Date executionDate = null;
        if(Objects.nonNull(triggers)) {
            for (Trigger trigger : triggers) {

                if(executionDate == null) {
                    executionDate = trigger.getStartTime();
                }

                Date previousFireTime = trigger.getPreviousFireTime();
                if (previousFireTime != null && previousFireTime.compareTo(executionDate) > 0) {
                    executionDate = previousFireTime;
                }
            }
        }
        return executionDate;
    }

    public static List<JobDataMap> getJobDataMapList(List<? extends Trigger> triggers) {
        return triggers.stream()
               .map(trigger -> trigger.getJobDataMap())
               .collect(Collectors.toList());
    }

    private Integer getInteger(Object object) {

        Integer value = null;
        try {
            if(object instanceof Integer) {
                value = (Integer) object;
            }

            if(object != null) {
                value = Integer.parseInt(object.toString());
            }
        }
        finally {
            return value;
        }
    }
}
