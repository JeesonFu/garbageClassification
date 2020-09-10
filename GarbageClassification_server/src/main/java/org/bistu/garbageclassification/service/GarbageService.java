package org.bistu.garbageclassification.service;

import java.util.List;

import org.bistu.garbageclassification.bean.Garbage;
import org.springframework.stereotype.Service;

@Service
public interface GarbageService {
	
	//垃圾类别详情页：根据所选类别，随机获取该类别的10个垃圾
	public List<Garbage> getGListByType(Integer type_id);
	
	//文字检索-精确查询，搜索结果页：查询并显示被搜索的垃圾详细信息
	public Garbage exactQuery(String name);

	//热门搜索榜单：获得搜索次数最多的前10个
	public List<Garbage> getTops();
	
	//搜索结果页：您要找的是不是，模糊查询
	public List<String> getSimilar(String name);
	
	//垃圾的搜索次数+1
	public void addSearchTimes(String name);
}
