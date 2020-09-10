package org.bistu.garbageclassification.entities;

import java.util.List;

/**
 * 垃圾类别
 */
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

	public Integer getType_id() {
		return type_id;
	}

	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_white() {
		return sign_white;
	}

	public void setSign_white(String sign_white) {
		this.sign_white = sign_white;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescrib() {
		return describ;
	}

	public void setDescrib(String describ) {
		this.describ = describ;
	}

	public List<String> getTips() {
		return tips;
	}

	public void setTips(List<String> tips) {
		this.tips = tips;
	}
}
