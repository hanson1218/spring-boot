package com.test.application.service;

import com.test.application.dao.TestDao;
//import com.test.datasource.aop.DynamicDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基于手动创建数据源，根据AOP的db 映射来切换数据源
 */
@Service
//@DynamicDatasource(dbIndex="111")
public class TestService {
    @Autowired
    private TestDao testDao;

    public int getName(String test,String test1){
        return testDao.getA();
    }


}
