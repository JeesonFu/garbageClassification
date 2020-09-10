package org.bistu.garbageclassification.bean;

import java.util.List;

import org.bistu.garbageclassification.bean.others.Option;
import org.bistu.garbageclassification.utils.AutoInc;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 题目信息
 * @author 付金振
 *
 */
@Document(collection = "question")
@Data
public class Question {

	@Indexed(unique = true)
	@AutoInc
	private Integer ques_id = 0;
	
	private String question;
	private List<Option> options;
	private String explain;
	private Integer true_times = 0;
	private Integer answer_times = 0;
	
	public Question() {
	}
	
	public Question(String question, List<Option> options, String explain) {
		this.question =question;
		this.options = options;
		this.explain = explain;
	}
}
