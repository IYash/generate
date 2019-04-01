package com.person.generate.db;


import com.person.mybatis.mapper.ShardBlogMapper;
import com.person.mybatis.model.ShardBlog;
import com.person.mybatis.model.ShardBlogExample;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;

public class ShardTest {
    String resource = "mybatis-config.xml";
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
        ShardBlogMapper blogMapper =session.getMapper(ShardBlogMapper.class);
        blogMapper.insert(createShardBlog());
        session.commit();
    }
    private ShardBlog createShardBlog(){
        ShardBlog blog=new ShardBlog();
        blog.setMessage("shard_blog_test_msg_type");
        blog.setMsgType(0);
        blog.setCreateTime(new Date());
        return blog;
    }
    @Test
    public void testSelect(){
        InputStream is= this.getClass().getClassLoader().getResourceAsStream(resource);
        //构建sqlsession工厂
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
        //使用mybatis提供更多resource类加载mybatis的配置文件
        //Reader reader = Resources.getResourceAsReader(resource);
        //SqlSessionFactory factory= new SqlSessionFactoryBuilder().build(reader);
        //创建能执行映射文件中sql的sqlsession
        SqlSession session = factory.openSession();
        ShardBlogMapper blogMapper =session.getMapper(ShardBlogMapper.class);
        ShardBlogExample example = new ShardBlogExample();
        example.setMsgType(0);
        ShardBlog blog = blogMapper.selectByExample(example).get(0);
        System.out.println(blog);
    }
}
