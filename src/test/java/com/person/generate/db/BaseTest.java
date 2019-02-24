package com.person.generate.db;

import com.person.mybatis.mapper.BlogMapper;
import com.person.mybatis.model.Blog;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;

public class BaseTest {


    String resource = "mybatis-config.xml";
    //测试mybatis的sql语句封装
    @Test
    public void testColumn() throws Exception{
        InputStream is= this.getClass().getClassLoader().getResourceAsStream(resource);
        //构建sqlsession工厂
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
        //使用mybatis提供更多resource类加载mybatis的配置文件
        //Reader reader = Resources.getResourceAsReader(resource);
        //SqlSessionFactory factory= new SqlSessionFactoryBuilder().build(reader);
        //创建能执行映射文件中sql的sqlsession
        SqlSession session = factory.openSession();
        BlogMapper blogMapper =session.getMapper(BlogMapper.class);
        blogMapper.insert(createBlog());
        session.commit();
    }
    private Blog createBlog(){
        Blog blog=new Blog();
        blog.setMessage("manual");
        blog.setCreateTime(new Date());
        return blog;
    }
}
