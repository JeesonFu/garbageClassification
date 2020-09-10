package org.bistu.garbageclassification.bean;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 城市
 * @author 付金振
 *
 */
@Document(collection = "city")
@Data
public class City {

	@Indexed(unique = true)
	private String city_name;
	
	public City() {
	}
	
	public City(String name) {
		this.city_name = name;
	}
}
