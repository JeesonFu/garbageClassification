package org.bistu.garbageclassification.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 维护自增id的实体，创建数据表存储当前id
 * 每个集合记录自己的自增id号
 * @author 付金振
 *（未使用）
 */
@Document(collection = "autoInc")
public class AutoIncInfo {

	@Id
	private String id; //主键
	
	@Field
	private String collName;  //需要自增id的集合名称
	
	@Field
	private Integer incId;   //该集合的当前自增id号

	public String getCollectionName() {
		return collName;
	}

	public void setCollectionName(String collectionName) {
		this.collName = collectionName;
	}

	public Integer getIncId() {
		return incId;
	}

	public void setIncId(Integer incId) {
		this.incId = incId;
	}
}
