package com.dgx.dianping.controller.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dgx.dianping.constant.DicTypeConst;
import com.dgx.dianping.constant.PageCodeEnum;
import com.dgx.dianping.dto.BusinessDto;
import com.dgx.dianping.service.BusinessService;
import com.dgx.dianping.service.DicService;

@Controller
@RequestMapping("/businesses")
public class BusinessesController {
	
	@Autowired
	private DicService dicService;
	
	@Autowired
	private BusinessService businessService;

	/**
	 * 商户列表
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String search(BusinessDto businessDto,Model model) {
		model.addAttribute("list", businessService.searchByPage(businessDto));
		model.addAttribute("searchParam", businessDto);
		return "/content/businessList";
	}
	
	/**
	 * 商户新增页初始化
	 */
	@RequestMapping(value="/addPage",method=RequestMethod.GET)
	public String addInit(Model model) {
		model.addAttribute("cityList", dicService.getListByType(DicTypeConst.CITY));
		model.addAttribute("categoryList", dicService.getListByType(DicTypeConst.CATEGORY));
		return "/content/businessAdd";
	}
	
	/**
	 * 新增商户
	 */
	@RequestMapping(method=RequestMethod.POST)
	public String add(BusinessDto businessDto,RedirectAttributes attributes) {
		if(businessService.add(businessDto)) {
			attributes.addAttribute(PageCodeEnum.KEY, PageCodeEnum.ADD_SUCCESS);
			return "redirect:/businesses";
		} else {
			attributes.addAttribute(PageCodeEnum.KEY, PageCodeEnum.ADD_FAIL);
			return "redirect:/businesses/addPage";
		}
	}
	
	/**
	 * 修改页初始化
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String modifyInit(@PathVariable("id")Long id,Model model) {
		model.addAttribute("cityList", dicService.getListByType(DicTypeConst.CITY));
		model.addAttribute("categoryList", dicService.getListByType(DicTypeConst.CATEGORY));
		model.addAttribute("modifyObj", businessService.getById(id));
		return "/content/businessModify";
	}
	
	/**
	 * 修改商户
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public String modify(@PathVariable("id")Long id,BusinessDto businessDto) {
		System.out.println(id);
		if(businessService.modify(businessDto)) {
			//
			return "redirect:/businesses";
		} else {
			//
			return "/content/businessModify";
		}
		// 可以使用重定向，以避免报请求方式PUT不支持的错误
	}
	
	/**
	 * 删除商户
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public String remove(@PathVariable("id")Long id) {
		System.out.println(id);
		return "redirect:/businesses";
	}
}
