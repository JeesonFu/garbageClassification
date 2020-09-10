package org.bistu.garbageclassification.bean;

import org.bistu.garbageclassification.utils.AutoInc;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 专题指南
 * @author 付金振
 *
 */
@Document(collection = "guide")
@Data
public class Guide {

	@Indexed(unique = true)
	@AutoInc
	private Integer guide_id = 0;
	
	private String title;
	private String content;
	
	public Guide() {
	}
	
	public Guide(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
