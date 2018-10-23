package com.dgx.dianping.mapper;

import java.util.List;

import com.dgx.dianping.pojo.Orders;

public interface OrdersMapper {

	/**
	 * 新增
	 * @param orders 订单表对象
	 * @return 影响行数
	 */
	int insert(Orders orders);
	
	/**
     * 根据主键查询订单表对象
     * @param id 主键值
     * @return 订单表对象
     */
	Orders selectById(Long id);
	
	/**
	 * 根据条件查询订单列表
	 * @param orders 查询条件
	 * @return 订单列表
	 */
	List<Orders> select(Orders orders);
	
	/**
	 * 更新
	 * @param orders 订单表对象
	 * @return 影响行数
	 */
	int update(Orders orders);
}
