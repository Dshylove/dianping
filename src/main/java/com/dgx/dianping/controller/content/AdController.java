package com.dgx.dianping.controller.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dgx.dianping.constant.PageCodeEnum;
import com.dgx.dianping.dto.AdDto;
import com.dgx.dianping.service.AdService;

@Controller
@RequestMapping("/ad")
public class AdController {

	@Autowired
	private AdService adService;
	
	/**
	 * 广告管理页初始化(点广告管理菜单进入的第一个页面)
	 */
	@RequestMapping
	public String init(Model model) {
		AdDto adDto = new AdDto();
		model.addAttribute("list", adService.searchByPage(adDto));
		model.addAttribute("searchParam", adDto);
		return "/content/adList";
	}
	
	/**
	 * 分页搜索广告列表
	 * @param adDto
	 * @param model
	 * @return
	 */
	@RequestMapping("/search")
	public String search(AdDto adDto,Model model) {
		model.addAttribute("list", adService.searchByPage(adDto));
		model.addAttribute("searchParam", adDto);
		return "/content/adList";
	}
	
	/**
	 * 新增页初始化
	 */
	@RequestMapping("/addInit")
	public String addInit() {
		return "/content/adAdd";
	}
	
	/**
	 * 新增广告
	 * @param adDto
	 * @return
	 */
	@RequestMapping("/add")
	public String add(AdDto adDto,Model model) {
		if(adService.add(adDto)) {
			model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.ADD_SUCCESS);
		} else {
			model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.ADD_FAIL);
		}
		return "/content/adAdd";
	}
	
	/**
	 * 修改页初始化
	 */
	@RequestMapping("/modifyInit")
	public String modifyInit(@RequestParam("id")Long id,Model model) {
		model.addAttribute("modifyObj", adService.getById(id));
		return "/content/adModify";
	}
	
	/**
	 * 修改广告
	 * @param adDto
	 * @param model
	 * @return
	 */
	@RequestMapping("/modify")
	public String modify(AdDto adDto,Model model) {
//		model.addAttribute("modifyObj", adDto); //直接回传提交数据，这里方法不对！！因为保存新图片后imgFileName会被改变
		if(adService.modify(adDto)) {
			model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.MODIFY_SUCCESS);
		} else {
			model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.MODIFY_FAIL);
		}
//		return "/content/adModify"; //如果修改完直接回到修改页，需要获得imgFileName的新值，即重新查询AdDto对象
		return "forward:/ad"; // 或者可以修改完直接转发到list页面重新查询~~
	}
	
	/**
	 * 删除广告
	 * @param id
	 * @return
	 */
	@RequestMapping("/remove")
	public String remove(@RequestParam("id")Long id,Model model) {
		if(adService.remove(id)) {
			model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.REMOVE_SUCCESS);
		} else {
			model.addAttribute(PageCodeEnum.KEY, PageCodeEnum.REMOVE_FAIL);
		}
		return "forward:/ad"; // 删除完转发到list页面
	}
}
