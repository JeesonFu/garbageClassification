package org.bistu.garbageclassification.entities;

import java.util.List;

import org.bistu.garbageclassification.entities.others.Option;

/**
 * 题目信息
 */
public class Question {

	private Integer ques_id;
	
	private String question;
	
	private List<Option> options;
	
	private String explain;
	
	private Integer true_times;
	
	private Integer answer_times;
	
	public Question() {
	}
	
	public Question(String question, List<Option> options, String explain) {
		this.question =question;
		this.options = options;
		this.explain = explain;
	}

	public Integer getQues_id() {
		return ques_id;
	}

	public void setQues_id(Integer ques_id) {
		this.ques_id = ques_id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public Integer getTrue_times() {
		return true_times;
	}

	public void setTrue_times(Integer true_times) {
		this.true_times = true_times;
	}

	public Integer getAnswer_times() {
		return answer_times;
	}

	public void setAnswer_times(Integer answer_times) {
		this.answer_times = answer_times;
	}
}
