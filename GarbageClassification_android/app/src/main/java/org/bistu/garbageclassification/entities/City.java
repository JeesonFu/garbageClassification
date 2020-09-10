package org.bistu.garbageclassification.entities;

/**
 * 城市
 */

public class City {

	private String city_name;
	
	public City() {
	}
	
	public City(String name) {
		this.city_name = name;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

}
