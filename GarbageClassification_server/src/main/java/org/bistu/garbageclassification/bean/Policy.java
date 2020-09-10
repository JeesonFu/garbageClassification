package org.bistu.garbageclassification.bean;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 条例表
 * @author 付金振
 *
 */
@Document(collection = "policy")
@Data
public class Policy {

	@Indexed(unique = true)
	private String policy_id;
	
	private String title;
	private String city;
	private String content;
	
	public Policy() {
	}
	
	public Policy(String pid, String title, String city, String content) {
		this.policy_id = pid;
		this.title = title;
		this.city = city;
		this.content = content;
	}
}
