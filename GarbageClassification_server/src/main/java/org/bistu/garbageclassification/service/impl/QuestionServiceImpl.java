package org.bistu.garbageclassification.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.bistu.garbageclassification.bean.Question;
import org.bistu.garbageclassification.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService{

	@Autowired
	private MongoTemplate mt;

	/**
	 * 单题作答情况记录：每当作答一题（点击答案选项）时，更新该题的作答计数
	 */
	@Override
	public void answer(Integer ques_id, boolean is_true) {
		Query query = new Query(Criteria.where("ques_id").is(ques_id));
		Update update = new Update().inc("answer_times", 1);
		if(is_true) {
			update.inc("true_times", 1);
		}
		mt.updateFirst(query, update, Question.class);	
	}

	//获取测试题目1道
	@Override
	public Question getQuestion() {
		List<Question> quesList = new ArrayList<Question>();
		Aggregation aggregation = Aggregation.newAggregation(Question.class, Aggregation.sample(1));
		AggregationResults<Question> questions = mt.aggregate(aggregation, Question.class, Question.class);
		questions.forEach((question) -> {
			quesList.add(question);
		});
		return quesList.get(0);
	}

}
