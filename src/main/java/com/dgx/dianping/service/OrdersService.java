package com.dgx.dianping.service;

import java.util.List;

import com.dgx.dianping.dto.OrdersDto;

/**
 * 订单业务层接口
 * @author dgx
 *
 */
public interface OrdersService {

	/**
	 * 新增订单
	 * @param ordersDto
	 * @return 是否新增成功：true：新增成功，false：新增失败
	 */
	public boolean add(OrdersDto ordersDto);
	
	/**
	 * 根据主键id获取订单的Dto对象
	 * @param id 订单表主键id值
	 * @return 订单的Dto对象
	 */
	public OrdersDto getById(Long id);
	
	/**
	 * 根据会员id获取会员订单列表
	 * @param memberId 会员id
	 * @return 会员订单列表
	 */
	public List<OrdersDto> getListByMemberId(Long memberId);
}
