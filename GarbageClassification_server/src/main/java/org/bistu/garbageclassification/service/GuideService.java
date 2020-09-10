package org.bistu.garbageclassification.service;

import java.util.List;

import org.bistu.garbageclassification.bean.Guide;
import org.springframework.stereotype.Service;

@Service
public interface GuideService {

	//获得专题指南列表，标题，默认5个
	public List<Guide> getGuides();
	
	//获取某一指南的详细内容
	public Guide getGuide(Integer guide_id);
}
