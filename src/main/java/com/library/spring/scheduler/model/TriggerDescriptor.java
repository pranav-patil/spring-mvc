package com.library.spring.scheduler.model;

import org.quartz.JobDataMap;
import org.quartz.Trigger;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

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

    /**
     *
     * @param trigger
     *            the Trigger used to build this descriptor
     * @return the TriggerDescriptor
     */
    public static TriggerDescriptor buildDescriptor(Trigger trigger) {
        return new TriggerDescriptor()
                .setName(trigger.getKey().getName())
                .setGroup(trigger.getKey().getGroup())
                .setFireTime((LocalDateTime) trigger.getJobDataMap().get("fireTime"))
                .setCron(trigger.getJobDataMap().getString("cron"));
    }

    public static Date getFutureExecutionDate(List<? extends Trigger> triggers) {
        Date executionDate = new Date(Long.MAX_VALUE);
        if(Objects.nonNull(triggers)) {
            for (Trigger trigger : triggers) {
                Date nextFireTime = trigger.getNextFireTime();
                if (nextFireTime.compareTo(executionDate) < 0) {
                    executionDate = nextFireTime;
                }
            }
        }
        return executionDate;
    }

    public static Date getLastExecutionDate(List<? extends Trigger> triggers) {
        Date executionDate = new Date(0);
        if(Objects.nonNull(triggers)) {
            for (Trigger trigger : triggers) {
                Date previousFireTime = trigger.getPreviousFireTime();
                if (previousFireTime.compareTo(executionDate) > 0) {
                    executionDate = previousFireTime;
                }
            }
        }
        return executionDate;
    }
}
