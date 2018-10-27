package com.dgx.dianping.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dgx.dianping.dto.UserDto;
import com.dgx.dianping.mapper.UserMapper;
import com.dgx.dianping.pojo.User;
import com.dgx.dianping.service.UserService;
import com.dgx.dianping.util.CommonUtil;
import com.dgx.dianping.util.MD5Util;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public boolean validate(UserDto userDto) {
		if (userDto != null && !CommonUtil.isEmpty(userDto.getName()) && !CommonUtil.isEmpty(userDto.getPassword())) {
			User user = new User();
			BeanUtils.copyProperties(userDto, user);
			List<User> list = userMapper.select(user);
			if (list.size() == 1) {
				BeanUtils.copyProperties(list.get(0), userDto);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<UserDto> getList() {
		List<UserDto> result = new ArrayList<>();
		List<User> userList = userMapper.select(new User());
		for(User user : userList) {
			UserDto userDto = new UserDto();
			result.add(userDto);
			BeanUtils.copyProperties(user, userDto);
			userDto.setpId(0);
		}
		return result;
	}

	@Override
	public boolean modify(UserDto userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		if(!CommonUtil.isEmpty(userDto.getPassword())) {
			user.setPassword(MD5Util.getMD5(userDto.getPassword()));
		}
		return userMapper.update(user) == 1;
	}

	@Override
	public boolean add(UserDto userDto) {
//		User user = new User();
//		BeanUtils.copyProperties(userDto, user);
		userDto.setPassword(MD5Util.getMD5(userDto.getPassword()));
		return userMapper.insert(userDto) == 1;
	}

	@Override
	public boolean remove(Long id) {
		return userMapper.delete(id) == 1;
	}

	@Override
	public UserDto getById(Long id) {
		UserDto userDto = new UserDto();
		User user = userMapper.selectById(id);
		BeanUtils.copyProperties(user, userDto);
		return userDto;
	}

}
