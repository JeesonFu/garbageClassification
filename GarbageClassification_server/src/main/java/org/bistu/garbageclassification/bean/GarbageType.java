package org.bistu.garbageclassification.bean;

import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 垃圾类别实体类
 * @author 付金振
 *
 */
@Document(collection = "garbage_type")
@CompoundIndexes({ //复合索引
	@CompoundIndex(name="tid_city", def="{'type_id':1, 'city':1}")
})
@Data
public class GarbageType {
	
	private Integer type_id;
	private String city;
	private String sign;
	private String sign_white;
	private String name;
	private String describ;
	private List<String> tips;
	
	public GarbageType() {
	}
	
	public GarbageType(Integer tid, String city, String sign, String sign_white, String name, String describ, List<String> tips) {
		this.type_id = tid;
		this.city = city;
		this.sign = sign;
		this.sign_white = sign_white;
		this.name = name;
		this.describ = describ;
		this.tips = tips;
	}
}
