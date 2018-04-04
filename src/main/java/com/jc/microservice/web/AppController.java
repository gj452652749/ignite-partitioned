package com.jc.microservice.web;

import com.jc.microservice.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wangjie
 * @Description:
 * @Date: Created in 14:56 2018/4/2
 */
@RestController
@RequestMapping("/ignite")
@CrossOrigin
public class AppController {
    @Autowired
    AppService appService;
    @RequestMapping("/get")
    @ResponseBody
    public String put(int key){
        String data = appService.get(key);
        return "ok,get data is [" + data +"],succeed!";
    }
    @RequestMapping("/put")
    @ResponseBody
    public String put(int key, String value){
        appService.put(key, value);
        return "ok";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "just for testing!";
    }
}
