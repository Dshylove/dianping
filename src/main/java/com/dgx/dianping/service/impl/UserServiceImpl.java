package com.dgx.dianping.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dgx.dianping.dto.UserDto;
import com.dgx.dianping.mapper.UserMapper;
import com.dgx.dianping.pojo.User;
import com.dgx.dianping.service.UserService;
import com.dgx.dianping.util.CommonUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public boolean validate(UserDto userDto) {
		if(userDto != null && !CommonUtil.isEmpty(userDto.getName()) && !CommonUtil.isEmpty(userDto.getPassword())) {
			User user = new User();
			BeanUtils.copyProperties(userDto, user);
			List<User> list = userMapper.select(user);
			if(list.size() == 1) {
				BeanUtils.copyProperties(list.get(0), userDto);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<UserDto> getList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean modify(UserDto userDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean add(UserDto userDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserDto getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
