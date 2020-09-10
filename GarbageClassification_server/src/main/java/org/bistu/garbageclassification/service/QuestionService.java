package org.bistu.garbageclassification.service;

import org.bistu.garbageclassification.bean.Question;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {
	
	//单题作答情况记录
	public void answer(Integer ques_id, boolean is_true);

	//获取一道题
	public Question getQuestion();

}
