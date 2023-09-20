package com.sml.mybatisdemo.dao;

import com.sml.mybatisdemo.domin.Worker;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WorkerDao {
    List<Worker> getAllWorkers();
    Worker getWorkerById(String id);
    int Update(@Param("Worker")Worker Worker);
}