package com.dgx.dianping.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dgx.dianping.mapper.BusinessMapper;

/**
 * 商户相关的定时任务
 * @author dgx
 *
 */
@Component("BusinessTask")
public class BusinessTask {

	private static final Logger logger = LoggerFactory.getLogger(BusinessTask.class);
	
	@Autowired
	private BusinessMapper businessMapper;
	
	/**
	 * 同步已售数量
	 */
	public void synNumber() {
		logger.info("已同步！");
	}
}
