package org.bistu.garbageclassification.service.impl;

import java.util.List;

import org.bistu.garbageclassification.bean.City;
import org.bistu.garbageclassification.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {
	
	@Autowired
	private MongoTemplate mt;
	
	/**
	 * 城市切换页：获取城市列表
	 */
	@Override
	public List<City> getAllCity() {
		return mt.findAll(City.class);
	}

}
