package com.dgx.dianping.service;

import java.util.List;

import com.dgx.dianping.dto.BusinessDto;
import com.dgx.dianping.dto.BusinessListDto;

/**
 * 商户业务层接口
 * @author dgx
 *
 */
public interface BusinessService {

	/**
	 * 新增商户
	 * @param businessDto
	 * @return
	 */
	public boolean add(BusinessDto businessDto);
	
	/**
	 * 根据id获取商户
	 * @param id
	 * @return
	 */
	public BusinessDto getById(Long id);
	
	/**
	 * 分页搜索商户列表
	 * @param businessDto 查询条件(包含分页对象)
	 * @return 商户列表
	 */
	public List<BusinessDto> searchByPage(BusinessDto businessDto);
	
	/**
	 * 分页搜索商户列表(Api接口专用)
	 * @param businessDto
	 * @return
	 */
	public BusinessListDto searchByPageForApi(BusinessDto businessDto);
	
	/**
	 * 删除商户
	 * @param id
	 * @return
	 */
	public boolean remove(Long id);
	
	/**
	 * 修改商户
	 * @param businessDto
	 * @return
	 */
	public boolean modify(BusinessDto businessDto);
}
