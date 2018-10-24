package com.dgx.dianping.service;

import com.dgx.dianping.dto.CommentForSubmitDto;
import com.dgx.dianping.dto.CommentListDto;
import com.dgx.dianping.pojo.Page;

public interface CommentService {

	/**
	 * 保存评论
	 * @param commentForSubmitDto 提交的评论Dto对象
	 * @return 是否保存成功：true：成功，false：失败
	 */
	boolean add(CommentForSubmitDto commentForSubmitDto);
	
	/**
	 * 按分页条件，根据商户id获取商户下的评论列表Dto
	 * @param businessId 商户id
	 * @param page 分页对象
	 * @return 评论列表Dto
	 */
	CommentListDto getListByBusinessId(Long businessId,Page page);
}
