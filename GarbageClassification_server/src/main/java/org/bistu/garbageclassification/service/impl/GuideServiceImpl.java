package org.bistu.garbageclassification.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.bistu.garbageclassification.bean.Guide;
import org.bistu.garbageclassification.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class GuideServiceImpl implements GuideService{

	@Autowired
	private MongoTemplate mt;
	
	//随机获取专题指南5个标题
	@Override
	public List<Guide> getGuides() {
		List<Guide> guideList = new ArrayList<Guide>();
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.project("guide_id","title"), Aggregation.sample(5));
		
		AggregationResults<Guide> guides = mt.aggregate(aggregation, Guide.class, Guide.class);
		guides.forEach((guide) -> {
			guideList.add(guide);
		});
		return guideList;
	}

	//根据指南id获取详情
	@Override
	public Guide getGuide(Integer guide_id) {
		Query query = new Query(new Criteria("guide_id").is(guide_id));
		return mt.findOne(query, Guide.class);
	}

}
