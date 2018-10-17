package com.dgx.dianping.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dgx.dianping.mapper.DicMapper;
import com.dgx.dianping.pojo.Dic;
import com.dgx.dianping.service.DicService;

@Service
public class DicServiceImpl implements DicService {

	@Autowired
	private DicMapper dicMapper;
	
	@Override
	public List<Dic> getListByType(String type) {
		Dic dic = new Dic();
		dic.setType(type);
		return dicMapper.select(dic);
	}

}
