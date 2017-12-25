package com.library.spring.web.mapper;

import com.library.spring.web.domain.ScheduleTask;
import com.library.spring.web.model.BatchTask;
import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;

import java.util.List;

@Mapper(
        withCustomFields = {
            @Field({"executiondate", "lastExecutionDate"})
        },
        withIgnoreFields = {"id", "jobName", "jobGroup", "action"},
        withIoC = IoC.SPRING
)
public interface ScheduleTaskMapper {

    ScheduleTask mapToScheduleTask(BatchTask batchTask);

    BatchTask mapToBatchTask(ScheduleTask entity);

    List<ScheduleTask> mapToScheduleTasks(List<BatchTask> batchTasks);

    List<BatchTask> mapToBatchTasks(List<ScheduleTask> scheduleTasks);
}
