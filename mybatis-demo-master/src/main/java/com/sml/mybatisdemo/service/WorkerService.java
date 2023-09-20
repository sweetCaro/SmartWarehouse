package com.sml.mybatisdemo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sml.mybatisdemo.domin.Worker;

import java.util.List;

public interface WorkerService {
    List<Worker> getAllWorkers();
    Worker getWorkerById(String id);
    JsonNode Update(Worker Worker) throws Exception;

}