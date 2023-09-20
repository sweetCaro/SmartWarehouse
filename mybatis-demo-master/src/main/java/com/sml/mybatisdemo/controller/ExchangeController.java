package com.sml.mybatisdemo.controller;

import com.sml.mybatisdemo.domin.Exchange;
import com.sml.mybatisdemo.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin

public class ExchangeController {
    @Autowired
    ExchangeService exchangeService;

    @GetMapping(value = "/exchanges")//显示所有进出仓货物的信息
    public List<Exchange> getAllExchange() {
        return exchangeService.getAllExchange();
    }


}