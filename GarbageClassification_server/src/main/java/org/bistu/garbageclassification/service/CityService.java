package org.bistu.garbageclassification.service;

import java.util.List;

import org.bistu.garbageclassification.bean.City;
import org.springframework.stereotype.Service;

@Service
public interface CityService {

	//城市切换页：获取城市列表
	public List<City> getAllCity();
	
}
