package com.test.application.service;

import com.test.application.dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测试读写分离的动态数据源，基于springboot实例化数据源 -- datasource2
 */
@Service
public class TestDataSource2Service {
    @Autowired
    private TestDao testDao;

    public int getName(){
        return testDao.getA();
    }

    public String setData(){
        String test1 = testDao.dddA()+"";
        return test1;
    }
}
