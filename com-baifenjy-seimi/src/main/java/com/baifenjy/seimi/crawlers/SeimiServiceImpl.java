package com.baifenjy.seimi.crawlers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baifenjy.seimi.entity.BlogContent;
import com.baifenjy.seimi.repository.ContentDao;

@Service
@Transactional(readOnly=true)
public class SeimiServiceImpl implements SeimiService
{
    @Autowired
    private ContentDao contentDao;

    
    @Override
    @Transactional(readOnly=false)
    public int save(BlogContent content)
    {
        return contentDao.save(content);
    }

    @Transactional(readOnly=false)
    public void addByBatchOfBlogContent(ArrayList<BlogContent> blogContents)
    {
        contentDao.addByBatchOfBlogContent(blogContents);
    }

}
