package com.baifenjy.seimi.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baifenjy.seimi.entity.BlogContent;
import com.mysql.jdbc.Statement;

@Repository
public class ContentDao
{
    Logger logger = LoggerFactory.getLogger(ContentDao.class);
    
    private static final String TABLE = "blog"; //id,content,title,update_time
    private static final String ID = "id";
    private static final String CONTENT = "content";
    private static final String TITLE = "title";
    private static final String UPDATE_TIME = "update_time";
    
    @Autowired
    private DataSource dataSource;

    public int save(BlogContent content){
        Connection conn = null;
        PreparedStatement pst = null;
        try
        {
            conn = dataSource.getConnection();
            String sql = String.format("insert into %s(%s,%s) values(?,?)", TABLE,CONTENT
                    ,TITLE);
            pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS );
            pst.setString(1, content.getContent());
            pst.setString(2, content.getTitle());
            int rowCount = pst.executeUpdate();
            if (rowCount>0){
                ResultSet generatedKeys = pst.getGeneratedKeys();
                while (generatedKeys.next()){
                   return generatedKeys.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("save user failed :"+content.toString(), e.toString());
            return -1;
        }finally{
            release(conn, pst, null);
        }
    
    }

    public void addByBatchOfBlogContent(ArrayList<BlogContent> blogContents)
    {
          String sql = String.format("insert into %s(%s,%s) values(?,?)", TABLE,CONTENT,TITLE);
            Connection conn = null;
            PreparedStatement pst = null;
            try{
                conn = dataSource.getConnection();
                pst = conn.prepareStatement(sql);
                conn.setAutoCommit(false);
                for(BlogContent round: blogContents)
                {
                    //write one round data
                    pst.setString(1, round.getTitle());
                    pst.setString(2,round.getContent());
                    pst.addBatch();
                }
                pst.executeBatch();
                conn.commit();
                conn.setAutoCommit(true);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                logger.error("", e);
            }finally{
                release(conn, pst, null);
            }
        }

    private void release(Connection conn, PreparedStatement ps, ResultSet rs)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
            }
            catch (Exception e)
            {
                logger.error("", e);
            }finally {
                rs = null;
            }
        }
        if (ps != null)
        {
            try
            {
                ps.close();
            }
            catch (Exception e)
            {
                logger.error("", e);
            }finally {
                ps = null;
            }
        }

        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (Exception e)
            {
                logger.error("", e);
            }finally {
                conn = null;
            }
        }
    }
	
}