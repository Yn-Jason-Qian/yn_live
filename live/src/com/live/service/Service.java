package com.live.service;

import java.util.Map;

/**
 * �����ṩ����ӿ�
 * @author qian_y
 *
 */
public interface Service {

	/**
	 * ��ʼ����������Ļ������
	 * 
	 */
	void init();
	
	/**
	 * ����ӿ�
	 * 
	 * @param serviceParams	
	 */
	void service(Map<String, Object> serviceParams);
	
}
