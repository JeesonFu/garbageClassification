package org.bistu.garbageclassification.service;

import java.util.List;

import org.bistu.garbageclassification.bean.GarbageType;
import org.springframework.stereotype.Service;

@Service
public interface GarbageTypeService {
	
	//根据city城市名 获取当前城市的四类垃圾信息预览
	public List<GarbageType> getTypesPre(String city);
	
	//根据type_id,city_id，获取类别详细信息
	public GarbageType getTypeDetail(Integer type_id, String city);

	
}
