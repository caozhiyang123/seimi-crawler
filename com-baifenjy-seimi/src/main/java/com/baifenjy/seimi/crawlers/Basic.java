package com.baifenjy.seimi.crawlers;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seimicrawler.xpath.JXDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baifenjy.seimi.controller.LoggerController;
import com.baifenjy.seimi.entity.BlogContent;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;

@Component
@Crawler(name = "basic_save")
public class Basic extends BaseSeimiCrawler 
{
    @Override
    public String[] startUrls() {
        return new String[]{"http://www.cnblogs.com/"};
    }
     @Override
     public void start(Response response) {
         JXDocument doc = response.document();
         try {
             List<Object> urls = doc.sel("//a[@class='titlelnk']/@href");
             logger.info("{}", urls.size());
             for (Object s:urls){
                 push(Request.build(s.toString(),Basic::getTitle));
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
     
     public void getTitle(Response response){
         JXDocument doc = response.document();
         try {
             logger.info("url:{} {}", response.getUrl(), doc.sel("//h1[@class='postTitle']/a/text()|//a[@id='cb_post_title_url']/text()"));
             String fileName = StringUtils.substringAfterLast(response.getUrl(),"/");
             String path = "C:/Users/12502/Desktop/abc/"+fileName;
             response.saveTo(new File(path));
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
     
    /* @Autowired
     private LoggerController loggerController;*/

     public void renderBean(Response response) {
         try {
             BlogContent blog = response.render(BlogContent.class);
             logger.info("bean resolve res={},url={}", blog, response.getUrl());
//             loggerController.add(blog);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
}