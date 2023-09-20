package com.sml.mybatisdemo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sml.mybatisdemo.domin.Worker;
import com.sml.mybatisdemo.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/workers")
public class WorkerController {
    @Autowired
    private WorkerService workerService;

    @GetMapping(value = "/getAllWorkers")//显示所有员工信息
    public List<Worker> getAllWorkers(){
        return workerService.getAllWorkers();
    }

    @GetMapping("/{id}")//按照员工ID号筛选指定员工信息
    public Worker getWorkerById(@PathVariable(name = "id") String id) {
        return workerService.getWorkerById(id);
    }

    //修改指定员工信息
    @RequestMapping(value = "/update", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public JsonNode Update (Worker Worker) throws Exception {
        return workerService.Update(Worker);
    }

}
