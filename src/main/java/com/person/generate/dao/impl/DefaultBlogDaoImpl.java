package com.person.generate.dao.impl;

import com.person.generate.dao.IBlogDao;
import com.person.generate.entity.Blog;
import com.person.generate.util.IRSHandler;
import com.person.generate.util.JDBCTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangchangling on 2017/10/16 0016
 */
public class DefaultBlogDaoImpl implements IBlogDao {



    public void insertBlog(Blog blog) {

        String sql = "insert into blog(message,create_Time) values (?,?)";

        Object[] paramtersVal = new Object[]{blog.getMessage(),blog.getCreateTime()};

        JDBCTemplate.update(sql,paramtersVal);


    }

    public Blog selectById(int id) {

        String sql = "select * from blog where id = ?";

        Object[] parametersVal = new Object[]{id};


        List<Blog> rets = JDBCTemplate.query(sql,new BlogResultHandlder(),parametersVal);

        return rets.get(0);
    }

    @Override
    public Blog query(String sql, Object... params) {
        return JDBCTemplate.query(sql,new BlogResultHandlder(),params).get(0);
    }

    class BlogResultHandlder implements IRSHandler<Blog> {

        @Override
        public List<Blog> handle(ResultSet rs) {
            List<Blog> result = null;
            if (rs != null) {
                result = new ArrayList<>();
                try {
                    while (rs.next()) {
                        Blog blog = new Blog();
                        blog.setId(rs.getInt(1));
                        blog.setMessage(rs.getString(2));
                        blog.setCreateTime(rs.getDate(3));
                        result.add(blog);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            return result;
        }
    }
}
