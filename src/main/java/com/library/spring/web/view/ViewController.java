package com.library.spring.web.view;

import com.library.spring.web.model.BatchTask;
import com.library.spring.web.service.MongoSyncBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/view")
public class ViewController {

    @Autowired
    private MongoSyncBatchService mongoSyncBatchService;

    @GetMapping("/batchschedule")
    public String home() {
        return "batchschedule";
    }

    @ModelAttribute("allBatchTasks")
    public List<BatchTask> allBatchTasks() throws Exception {
        return mongoSyncBatchService.getAllMongoSyncJobs();
    }
}