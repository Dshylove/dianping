package com.dgx.dianping.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dgx.dianping.constant.CommentStateConst;
import com.dgx.dianping.dto.CommentDto;
import com.dgx.dianping.dto.CommentForSubmitDto;
import com.dgx.dianping.dto.CommentListDto;
import com.dgx.dianping.mapper.CommentMapper;
import com.dgx.dianping.mapper.OrdersMapper;
import com.dgx.dianping.pojo.Business;
import com.dgx.dianping.pojo.Comment;
import com.dgx.dianping.pojo.Orders;
import com.dgx.dianping.pojo.Page;
import com.dgx.dianping.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private OrdersMapper ordersMapper;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,
	isolation = Isolation.READ_COMMITTED)
	public boolean add(CommentForSubmitDto commentForSubmitDto) {
		Comment comment = new Comment();
		BeanUtils.copyProperties(commentForSubmitDto, comment);
		comment.setId(null);
		comment.setOrdersId(commentForSubmitDto.getId());
		comment.setCreateTime(new Date());
		// 保存评论
		if(1 == commentMapper.insert(comment)) {
			// 更新订单评论状态
			Orders orders = new Orders();
			orders.setId(commentForSubmitDto.getId());
			orders.setCommentState(CommentStateConst.HAS_COMMENT);
			ordersMapper.update(orders);
			return true;
		}
		return false;
	}

	@Override
	public CommentListDto getListByBusinessId(Long businessId, Page page) {
		CommentListDto result = new CommentListDto();
		
		// 组织查询条件
		Comment comment = new Comment();
		Orders orders = new Orders();
		Business business = new Business();
		// 评论里包含了订单对象
		comment.setOrders(orders);
		// 订单对象里包含了商户对象
		orders.setBusiness(business);
		// 设置商户主键
		business.setId(businessId);
		// 前端app页码从0开始计算，这里需要+1
		page.setCurrentPage(page.getCurrentPage() + 1);
		// 设置分页条件
		comment.setPage(page);
		
		List<Comment> commentList = commentMapper.selectByPage(comment);
		// 组织返回值
		List<CommentDto> data = new ArrayList<>();
		result.setData(data);
		for(Comment temp : commentList) {
			CommentDto commentDto = new CommentDto();
			data.add(commentDto);
			BeanUtils.copyProperties(temp, commentDto);
			// 隐藏手机号中间4位
			StringBuffer phoneBuffer = new StringBuffer(String.valueOf(temp.getOrders().getMember().getPhone()));
			commentDto.setUsername(phoneBuffer.replace(3, 7, "****").toString());
			commentDto.getOrders().getMember().setPhone(null);	// 将不必要传输的参数属性置空
		}
		result.setHasMore(page.getCurrentPage() < page.getTotalPage());
		return result;
	}

}
