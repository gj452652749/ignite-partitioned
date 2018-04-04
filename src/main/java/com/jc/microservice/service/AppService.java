package com.jc.microservice.service;

import com.jc.microservice.dao.AppDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: wangjie
 * @Description:
 * @Date: Created in 14:52 2018/4/2
 */
@Service
public class AppService {
    @Resource
    AppDao appDao;

    public String get(int key){
        return appDao.get(key);
    }

    public void put(int key,String value){
        appDao.put(key, value);
    }
}
