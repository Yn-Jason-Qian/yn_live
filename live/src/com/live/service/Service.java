package com.live.service;

import java.util.Map;

/**
 * 对外提供服务接口
 * @author qian_y
 *
 */
public interface Service {

	/**
	 * 初始化服务所需的基础组件
	 * 
	 */
	void init();
	
	/**
	 * 服务接口
	 * 
	 * @param serviceParams	
	 */
	void service(Map<String, Object> serviceParams);
	
}
