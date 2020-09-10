package org.bistu.garbageclassification.service.impl;

import org.bistu.garbageclassification.bean.Policy;
import org.bistu.garbageclassification.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class PolicyServiceImpl implements PolicyService{

	@Autowired
	private MongoTemplate mt;
	
	//根据城市查询条例信息
	@Override
	public Policy getPolicyByCity(String city) {
		Query query = new Query(new Criteria("city").is(city));
		return mt.findOne(query, Policy.class);
	}
	
}
