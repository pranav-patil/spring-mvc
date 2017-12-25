package com.library.spring.web.controller;

import com.library.spring.web.model.BatchTask;
import com.library.spring.web.service.MongoSyncBatchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Autowired
    private MongoSyncBatchService mongoSyncBatchService;

    @GetMapping("/jobs")
    @ApiOperation(value = "Get Batch Jobs", notes = "Retrieve all batch jobs.", response = BatchTask.class, responseContainer = "List")
    public List<BatchTask> getJobs() throws Exception {
        return mongoSyncBatchService.getAllMongoSyncJobs();
    }

    @PostMapping("/job/operation")
    @ApiOperation(value = "BatchJob Operation", notes = "Retrieve weather for city by name.", response = BatchTask.class)
    public BatchTask jobOperation(@ApiParam(required = true, name = "BatchJobAction object",
                                         value = "Attributes and action to perform on batch job") BatchTask batchTask) throws Exception {

        if("edit".equals(batchTask.getAction())) {
            mongoSyncBatchService.updateMongoSyncJob(batchTask);
        } else if("delete".equals(batchTask.getAction())) {
            mongoSyncBatchService.deleteMongoSyncJob(batchTask);
        } else if("restore".equals(batchTask.getAction())) {
            mongoSyncBatchService.addMongoSyncJob(batchTask);
        }

        return batchTask;
    }
}
