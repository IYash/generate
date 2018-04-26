package com.person.generate.dao;

import com.person.generate.dao.impl.DefaultBlogDaoImpl;
import com.person.generate.entity.Blog;
import com.person.generate.query.BlogQuery;
import org.junit.Test;

import java.util.Date;

/**
 * @author huangchangling on 2017/10/16 0016
 */
public class BlogDaoTest {

    private IBlogDao blogDao = new DefaultBlogDaoImpl();

    @Test
    public void testInsert(){
        Blog blog = new Blog();
        blog.setMessage("dao test");
        blog.setCreateTime(new Date());
        blogDao.insertBlog(blog);
    }
    @Test
    public void testSelect(){
        int id = 33;
        Blog blog = blogDao.selectById(id);
        System.out.println(blog);
    }
    @Test
    public void testQuery(){
        BlogQuery query = new BlogQuery();
        //query.setId(2);
        //query.setMessage("init");
        StringBuilder sqlBuilder = new StringBuilder(100);
        sqlBuilder.append("select * from blog ").append(query.getQuery());
        String sql = sqlBuilder.toString();
        Blog blog = blogDao.query(sql,query.getParams());
        System.out.println(sql);
        System.out.println(blog);
    }
}
