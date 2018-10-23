package com.dgx.dianping.controller.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dgx.dianping.dto.AdDto;
import com.dgx.dianping.dto.OrdersDto;
import com.dgx.dianping.service.MemberService;
import com.dgx.dianping.service.OrdersService;

@Controller
@RequestMapping("/orders")
public class OrdersController {

	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String init(OrdersDto ordersDto,Model model) {
		model.addAttribute("list", ordersService.getListByMemberId(null));
		model.addAttribute("searchParam", ordersDto);
		return "/content/orderList";
	}
	
	/**
	 * 根据手机号分页查询订单列表
	 * @param adDto
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public String search(@RequestParam("username")Long username,Model model) {
		OrdersDto ordersDto = new OrdersDto();
		Long memberId = memberService.getIdByPhone(username);
		if(memberId != null) {	// 会员主键id不为空才查询
			model.addAttribute("list", ordersService.getListByMemberId(memberId));
			model.addAttribute("searchParam", ordersDto);
		}
		return "/content/orderList";
	}
}
