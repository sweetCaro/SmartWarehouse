package com.sml.mybatisdemo.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sml.mybatisdemo.dao.WorkerDao;
import com.sml.mybatisdemo.domin.Worker;
import com.sml.mybatisdemo.service.WorkerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WorkerServiceImpl implements WorkerService {
    @Resource
    WorkerDao workerDao;
    @Override
    public List<Worker> getAllWorkers() {
        return workerDao.getAllWorkers();
    }
    @Override
    public Worker getWorkerById(String id) {
        return workerDao.getWorkerById(id);
    }

    public static JsonNode stringToJSONObject(String jsonString) throws Exception {
        ObjectMapper jacksonObjMapper = new ObjectMapper();
        return jacksonObjMapper.readTree(jsonString);
    }
    @Override
    public JsonNode Update(Worker Worker) throws Exception {
        String jsonString = "{\"code\":\"200\"}";
        int a = workerDao.Update(Worker);
        if(a ==1)
            return stringToJSONObject(jsonString);
        else
            return stringToJSONObject("{\"code\":\"500\"}");
    }
    //传回Json格式信息，修改成功code为200，修改不成功code为500
}