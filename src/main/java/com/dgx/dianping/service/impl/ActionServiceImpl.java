package com.dgx.dianping.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dgx.dianping.dto.ActionDto;
import com.dgx.dianping.mapper.ActionMapper;
import com.dgx.dianping.pojo.Action;
import com.dgx.dianping.service.ActionService;

@Service
public class ActionServiceImpl implements ActionService {
	
	@Autowired
	private ActionMapper actionMapper;
	
	@Override
	public boolean add(ActionDto dto) {
		return actionMapper.insert(dto) == 1;
	}

	@Override
	public boolean remove(Long id) {
		return actionMapper.deleteById(id) == 1;
	}

	@Override
	public boolean modify(ActionDto dto) {
		Action action = new Action();
		BeanUtils.copyProperties(dto,action);
		return actionMapper.update(action) == 1;
	}

	@Override
	public ActionDto getById(Long id) {
		ActionDto result = new ActionDto();
		Action action = actionMapper.selectById(id);
		BeanUtils.copyProperties(action, result);
		return result;
	}
}
