package com.dgx.dianping.mapper;

import java.util.List;

import com.dgx.dianping.pojo.Business;

public interface BusinessMapper {

	public int insert(Business business);
	
	public Business selectById(Long id);
	
	/**
	 * 根据查询条件分页查询商户列表
	 * @param business
	 * @return
	 */
	public List<Business> selectByPage(Business business);
	
	/**
	 *  根据查询条件分页查询商户列表 : 
     *  标题、副标题、描述三个过滤条件为模糊查询
     *  并且这三个过滤条件之间为或者的关系，用 OR 连接
     *  这三个过滤条件与其他过滤条件依然是并且关系，用 AND 连接
	 * @param business
	 * @return
	 */
	public List<Business> selectLikeByPage(Business business);
	
	public int delete(Long id);
	
	public int update(Business business);
}
