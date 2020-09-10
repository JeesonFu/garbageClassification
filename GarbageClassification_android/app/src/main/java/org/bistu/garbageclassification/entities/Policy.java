package org.bistu.garbageclassification.entities;

/**
 * 政策表
 */
public class Policy {

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

	public String getPolicy_id() {
		return policy_id;
	}

	public void setPolicy_id(String policy_id) {
		this.policy_id = policy_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
