package com.person.mybatis.mapper;

import com.person.mybatis.model.ShardBlog;
import com.person.mybatis.model.ShardBlogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShardBlogMapper {
    int countByExample(ShardBlogExample example);

    int deleteByExample(ShardBlogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShardBlog record);

    int insertSelective(ShardBlog record);
    List<ShardBlog> selectByMsgType(ShardBlog record);

    List<ShardBlog> selectByExample(ShardBlogExample example);

    ShardBlog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShardBlog record, @Param("example") ShardBlogExample example);

    int updateByExample(@Param("record") ShardBlog record, @Param("example") ShardBlogExample example);

    int updateByPrimaryKeySelective(ShardBlog record);

    int updateByPrimaryKey(ShardBlog record);
}