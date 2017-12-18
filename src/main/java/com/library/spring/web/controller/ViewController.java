package com.library.spring.web.controller;

import com.library.spring.web.model.BatchTask;
import com.library.spring.web.service.BatchTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private BatchTaskService batchTaskService;

    @GetMapping("/batchschedule")
    public String home() {
        return "batchschedule";
    }

    @ModelAttribute("allBatchTasks")
    public List<BatchTask> populateFeatures() {
        return batchTaskService.getAllBatchTasks();
    }
}