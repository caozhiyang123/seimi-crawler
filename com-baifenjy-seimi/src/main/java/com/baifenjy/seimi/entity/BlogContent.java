package com.baifenjy.seimi.entity;

import cn.wanghaomiao.seimi.annotation.Xpath;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Xpath语法可以参考 http://jsoupxpath.wanghaomiao.cn/
 */
@Entity
@Table(name = "blog") //映射的表名称
public class BlogContent implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Xpath("//h1[@class='postTitle']/a/text()|//a[@id='cb_post_title_url']/text()")
    @Column(unique=false,name="title",columnDefinition="varchar(50) not null")
    private String title;

    //也可以这么写 @Xpath("//div[@id='cnblogs_post_body']//text()")
    @Xpath("//div[@id='cnblogs_post_body']/allText()")
    @Column(unique=false,name="content",columnDefinition="varchar(50) not null")
    private String content;
    
    @Column(unique=false,name="update_time",columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Timestamp update_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        if (StringUtils.isNotBlank(content)&&content.length()>100){
            //方便查看截断下
            this.content = StringUtils.substring(content,0,100)+"...";
        }
        return ToStringBuilder.reflectionToString(this);
    }
}
