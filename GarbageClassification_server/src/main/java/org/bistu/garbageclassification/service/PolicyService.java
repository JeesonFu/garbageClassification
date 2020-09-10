package org.bistu.garbageclassification.service;

import org.bistu.garbageclassification.bean.Policy;
import org.springframework.stereotype.Service;

/**
 * 关于政策的查询
 * @author 付金振
 *
 */
@Service
public interface PolicyService {
	
	//根据城市查询条例信息
	public Policy getPolicyByCity(String city);
	
}
