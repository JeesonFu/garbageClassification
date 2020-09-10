package org.bistu.garbageclassification.bean;

import org.bistu.garbageclassification.utils.AutoInc;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 垃圾实体类
 * @author 付金振
 *
 */
@Document(collection = "garbage")
@Data
public class Garbage {

	@AutoInc
	private Integer garbage_id = 0;
	
	@Indexed(unique = true)
	private String name;
	
	private Integer type_id;
	
	private Integer search_times;
	
	private String tips;
	
	@Transient
	private String type_name; //垃圾类别的名称，不写入数据库，仅作为普通bean属性
	
	public Garbage() {
	}
	
	public Garbage(String name, Integer tid) {
		this.name = name;
		this.type_id = tid;
	}
}
