package com.dgx.dianping.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码缓存，存放用户手机号与所下发的验证码
 * @author dgx
 *
 */
public class CodeCache {

	private static CodeCache instance;
	
	private Map<Long,String> codeMap;

	private CodeCache() {
		codeMap = new HashMap<>();
	}

	// 需要解决用户高并发行为
	public static CodeCache getInstance() {
		if(instance == null) {
			// 加锁，保证instance不会被重复实例化
			synchronized (CodeCache.class) {
				if(instance == null) { // 因为在高并发下，用户可能会同时进入该if语句块内
					instance = new CodeCache();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 保存手机号与验证码
	 * @param phone
	 * @param code
	 * @return true：保存成功，false：保存失败，手机号已存在
	 */
	public boolean save(Long phone,String code) {
		if(codeMap.containsKey(phone)) {
			return false;
		}
		codeMap.put(phone, code);
		return true;
	}
	
	/**
	 * 根据手机号获取验证码
	 * @param phone
	 * @return 验证码
	 */
	public String getCode(Long phone) {
		return codeMap.get(phone);
	}
}
