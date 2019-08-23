package com.baifenjy.seimi.controller;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baifenjy.seimi.crawlers.SeimiServiceImpl;
import com.baifenjy.seimi.entity.BlogContent;

public class LoggerController
{
    private static Logger logger = Logger.getLogger(LoggerController.class);
    private static ConcurrentLinkedQueue<BlogContent> blogContentQueue = new ConcurrentLinkedQueue<BlogContent>();
    static{
        logger.info("= = = = = = = = Starting ScheduledExecutorService BlogContentBatchInsert = = = = = = = = ");
        new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(new BlogContentBatchInsert(1000), 3, 5, TimeUnit.SECONDS);
    }
    
    private static class BlogContentBatchInsert implements Runnable{

        private int maxInsertRows = 1000;
        public BlogContentBatchInsert(int maxInsertRows)
        {
            this.maxInsertRows = maxInsertRows;
        }
        @Override
        public void run()
        {
         // save BlogContent to db
            ArrayList<BlogContent> multiBingoWinnerStatistics = new ArrayList<>();
            BlogContent multiBingoWinnerStatistic = blogContentQueue.poll();
            while (multiBingoWinnerStatistic != null)
            {
                multiBingoWinnerStatistics.add(multiBingoWinnerStatistic);
                multiBingoWinnerStatistic = blogContentQueue.poll();
                if (multiBingoWinnerStatistics.size() >= maxInsertRows)
                {
                    saveBlogContentsBatch(multiBingoWinnerStatistics);
                    multiBingoWinnerStatistics.clear();
                }
            }
            saveBlogContentsBatch(multiBingoWinnerStatistics);
        }
    }
    
    @Autowired
    private static SeimiServiceImpl contentService;
    
    private static void saveBlogContentsBatch(ArrayList<BlogContent> BlogContents) {
        if (BlogContents.size() != 0){
            logger.info("-----------------batch insert BlogContent begin-----------------");
        }
        contentService.addByBatchOfBlogContent(BlogContents);
    }

    public void add(BlogContent blog)
    {
        blogContentQueue.add(blog);
    }
}
