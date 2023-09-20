package com.sml.mybatisdemo.dao;

import com.sml.mybatisdemo.domin.Exchange;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExchangeDao {
    List<Exchange> getAllExchange();
}