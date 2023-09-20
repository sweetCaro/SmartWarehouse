package com.sml.mybatisdemo.mapper;

import com.sml.mybatisdemo.dao.ExchangeDao;
import com.sml.mybatisdemo.domin.Exchange;
import com.sml.mybatisdemo.service.ExchangeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    @Resource
    ExchangeDao exchangeDao;

    @Override
    public List<Exchange> getAllExchange() {
        return exchangeDao.getAllExchange();
    }
}