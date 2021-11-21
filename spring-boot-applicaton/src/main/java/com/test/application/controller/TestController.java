package com.test.application.controller;


import com.test.application.service.TestDataSource2Service;
import com.test.application.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private TestService testService;

    @Autowired
    private TestDataSource2Service testDataSource2Service;


    @GetMapping("feign")
    public String feign(){
        return "success";
    }

    @RequestMapping("test")
    public String getTest(){
        return testService.getName("1","2")+"";
    }

    @RequestMapping("data")
    public String getData(){
        return testDataSource2Service.getName()+"";
    }

    @RequestMapping("savedata")
    public String saveData(){
        return testDataSource2Service.setData()+"";
    }
}
