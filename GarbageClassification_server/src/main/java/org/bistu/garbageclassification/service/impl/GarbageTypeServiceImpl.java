package org.bistu.garbageclassification.service.impl;

import java.util.List;

import org.bistu.garbageclassification.bean.GarbageType;
import org.bistu.garbageclassification.service.GarbageTypeService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class GarbageTypeServiceImpl implements GarbageTypeService{
	
	@Autowired
	private MongoTemplate mt;

	//根据city城市名 获取当前城市的四类垃圾信息预览
	@Override
	public List<GarbageType> getTypesPre(String city) {
		Document condition = new Document();//查询条件
		condition.put("city", city);
		Document back = new Document();
		back.put("type_id", 1);
		back.put("city", 1);
		back.put("sign", 1);
		back.put("name", 1);
		Query query = new BasicQuery(condition, back);
		
		return mt.find(query, GarbageType.class);
	}

	//根据type_id,city_id，获取类别详情
	@Override
	public GarbageType getTypeDetail(Integer type_id, String city) {
		Query query = new Query(Criteria.where("type_id").is(type_id).and("city").is(city));
		return mt.findOne(query, GarbageType.class);
	}

}
