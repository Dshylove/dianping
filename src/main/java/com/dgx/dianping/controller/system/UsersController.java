package com.dgx.dianping.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dgx.dianping.constant.PageCodeEnum;
import com.dgx.dianping.dto.PageCodeDto;
import com.dgx.dianping.dto.UserDto;
import com.dgx.dianping.service.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserService userService;

	/**
	 * 获取用户列表
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public List<UserDto> getList() {
		return userService.getList();
	}
	
	/**
	 * 新增用户
	 */
	@RequestMapping(method=RequestMethod.POST)
	public PageCodeDto add(UserDto userDto) {
		PageCodeDto result;
		if(userService.add(userDto)) {
			result = new PageCodeDto(PageCodeEnum.ADD_SUCCESS);
		} else {
			result = new PageCodeDto(PageCodeEnum.USERNAME_EXISTS);	// 用户名已存在！
		}
		return result;
	}
	
	/**
	 * 根据主键获取用户dto
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public UserDto getById(@PathVariable("id")Long id) {
		return userService.getById(id);
	}
	
	/**
	 * 修改用户
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public PageCodeDto modify(UserDto userDto) {
		PageCodeDto result;
		if(userService.modify(userDto)) {
			result = new PageCodeDto(PageCodeEnum.MODIFY_SUCCESS);
		} else {
			result = new PageCodeDto(PageCodeEnum.MODIFY_FAIL);
		}
		return result;
	}
	
	/**
	 * 删除用户
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public PageCodeDto remove(@PathVariable("id")Long id) {
		PageCodeDto result;
		if(userService.remove(id)) {
			result = new PageCodeDto(PageCodeEnum.REMOVE_SUCCESS);
		} else {
			result = new PageCodeDto(PageCodeEnum.REMOVE_FAIL);
		}
		return result;
	}
}
