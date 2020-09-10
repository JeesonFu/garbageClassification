package org.bistu.garbageclassification.entities;

/**
 * 垃圾
 */
public class Garbage {

	private Integer garbage_id;

	private String name;
	
	private Integer type_id;
	
	private Integer search_times;
	
	private String tips;

	private String type_name;
	
	public Garbage() {
	}
	
	public Garbage(String name, Integer tid) {
		this.name = name;
		this.type_id = tid;
	}

	public Integer getGarbage_id() {
		return garbage_id;
	}

	public void setGarbage_id(Integer garbage_id) {
		this.garbage_id = garbage_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType_id() {
		return type_id;
	}

	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	public Integer getSearch_times() {
		return search_times;
	}

	public void setSearch_times(Integer search_times) {
		this.search_times = search_times;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
}
