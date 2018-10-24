package com.dgx.dianping.mapper;

import java.util.List;

import com.dgx.dianping.pojo.Comment;

public interface CommentMapper {

	/**
	 * 新增
	 * @param comment
	 * @return 影响的行数
	 */
	int insert(Comment comment);
	
	/**
	 * 根据查询条件分页查询
	 * @param comment
	 * @return 评论列表
	 */
	List<Comment> selectByPage(Comment comment);
}
