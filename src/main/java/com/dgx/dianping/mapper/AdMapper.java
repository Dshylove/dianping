package com.dgx.dianping.mapper;

import java.util.List;

import com.dgx.dianping.pojo.Ad;

public interface AdMapper {

	/**
	 * 新增
	 * @param ad
	 * @return 影响的行数
	 */
	public int insert(Ad ad);
	
	/**
	 * 根据查询条件分页查询
	 * @param ad
	 * @return
	 */
	public List<Ad> selectByPage(Ad ad);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public Ad selectById(Long id);
	
	/**
	 * 根据id删除
	 * @param id
	 * @return 影响的行数
	 */
	public int delete(Long id);
	
	/**
	 * 根据id更新
	 * @param ad
	 * @return 影响的行数
	 */
	public int update(Ad ad);
}
