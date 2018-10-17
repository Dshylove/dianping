package com.dgx.dianping.service;

import java.util.List;

import com.dgx.dianping.dto.AdDto;

/**
 * 广告业务层接口
 * @author dgx
 *
 */
public interface AdService {

	/**
	 * 新增广告
	 * @param adDto
	 * @return
	 */
	public boolean add(AdDto adDto);
	
	/**
	 * 分页搜索广告列表
	 * @param adDto 查询条件（包含分页对象）
	 * @return
	 */
	public List<AdDto> searchByPage(AdDto adDto);
	
	/**
	 * 根据id获取广告
	 * @param id
	 * @return
	 */
	public AdDto getById(Long id);
	
	/**
	 * 删除广告
	 * @param id
	 * @return
	 */
	public boolean remove(Long id);
	
	/**
	 * 修改广告
	 * @param adDto
	 * @return
	 */
	public boolean modify(AdDto adDto);
}
