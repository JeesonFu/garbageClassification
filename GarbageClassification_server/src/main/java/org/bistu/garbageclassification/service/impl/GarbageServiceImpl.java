package org.bistu.garbageclassification.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bistu.garbageclassification.bean.Garbage;
import org.bistu.garbageclassification.service.GarbageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GarbageServiceImpl implements GarbageService{

	@Autowired
	private MongoTemplate mt;
	
	//垃圾类别详情页：根据所选类别，随机获取该类别的10个垃圾
	@Override
	public List<Garbage> getGListByType(Integer type_id) {
		List<Garbage> gList = new ArrayList<Garbage>();
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.project("garbage_id","name","type_id"),
				Aggregation.match(Criteria.where("type_id").is(type_id)),
						Aggregation.sample(10));
		
		AggregationResults<Garbage> garbages = mt.aggregate(aggregation, Garbage.class, Garbage.class);
		garbages.forEach((garbage) -> {
			gList.add(garbage);
			//log.info(garbage.toString());
		});
		
		return gList;
	}
	
	//文字检索-精确查询，搜索结果页：查询并显示被搜索的垃圾详细信息
	@Override
	public Garbage exactQuery(String name) {
		Query query = new Query(Criteria.where("name").is(name));
		Garbage garbage = mt.findOne(query, Garbage.class);
		
		if(garbage != null) {
			addSearchTimes(name);
		}
			
		return garbage;
	}

	//热门搜索榜单：获得搜索次数最多的前10个
	@Override
	public List<Garbage> getTops() {
		Query query = new Query(Criteria.where("search_times").ne("").ne(null));
		
		return mt.find(query.with(Sort.by(Sort.Direction.DESC, "search_times")).limit(10), Garbage.class);
	}

	/*
	 * 搜索结果页：您要找的是不是，模糊查询
	 * 结果个数可能为（最多5个或6个）
	 */
	@Override
	public List<String> getSimilar(String name) {
		Query query = new Query(Criteria.where("name").regex(".*"+name+".*"));
		List<String> gName = mt.find(query.with(Sort.by(Sort.Direction.DESC, "search_times")).limit(6), Garbage.class)
				.stream()
				.map(Garbage::getName)
				.collect(Collectors.toList());
		gName.remove(name);//如果6条记录里有被搜索的关键词则删掉
		//log.info("" + gName);
		return gName;
	}

	//搜索次数+1
	@Override
	public void addSearchTimes(String name) {
		Query gQuery = new Query(Criteria.where("name").is(name));
		Update gUpdate = new Update().inc("search_times", 1);
		mt.updateFirst(gQuery, gUpdate, Garbage.class);
	}

}
