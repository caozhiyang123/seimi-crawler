package com.baifenjy.seimi.controller;


import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.wanghaomiao.seimi.core.Seimi;
import cn.wanghaomiao.seimi.spring.common.CrawlerCache;
import cn.wanghaomiao.seimi.struct.CrawlerModel;
import cn.wanghaomiao.seimi.struct.Request;



@RequestMapping(value="/seimi",method=RequestMethod.GET)
@Controller
@SuppressWarnings("unused")
public class SeimiController
{
    private static Logger logger = Logger.getLogger(SeimiController.class);
    
    private  String name;
    
    @GetMapping("/start")
    public String queryByOrderId(@RequestParam(value="name",required=true) String name){
       this.name = name;
       start();
       return "started";
    }
    
    @RequestMapping(value = "send_req")
    public String sendRequest(Request request){
        CrawlerCache.consumeRequest(request);
        return "consume suc";
    }
    
    @RequestMapping(value = "/info/{cname}")
    public String crawler(@PathVariable String cname) {
        CrawlerModel model = CrawlerCache.getCrawlerModel(cname);
        if (model == null) {
            return "not find " + cname;
        }
        return model.queueInfo();
    }

    private  void start()
    {
        try {
			Seimi seimi = new Seimi();
			if(name== null){
				return;
			}
			seimi.start(name);
//        seimi.goRun(name);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
    }
}
