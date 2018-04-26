package com.person.generate.dao;

import com.person.generate.entity.Blog;

import java.util.List;

/**
 * @author huangchangling on 2017/10/16 0016
 */
public interface IBlogDao {

    void insertBlog(Blog blog);

    Blog selectById(int id);

    Blog query(String sql,Object... params);
}
