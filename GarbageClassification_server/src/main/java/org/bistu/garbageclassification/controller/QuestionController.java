package org.bistu.garbageclassification.controller;

import org.bistu.garbageclassification.bean.Question;
import org.bistu.garbageclassification.service.QuestionService;
import org.bistu.garbageclassification.utils.ResultCode;
import org.bistu.garbageclassification.utils.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "关于答题测试的接口")
@RequestMapping(value = "/question")  
@RestController
public class QuestionController {
	
	@Autowired
	private QuestionService qService;
	
	@ApiOperation(value = "单题作答情况记录", notes = "记录作答情况", produces="application/json") 
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "Integer", name = "ques_id", value = "题目id", required = true),
		@ApiImplicitParam(dataType = "boolean", name = "is_true", value = "作答情况", required = true)
	})
	@RequestMapping(value = "/answer", method = RequestMethod.POST) 
	public ResultInfo<String> answer(@RequestParam Integer ques_id, @RequestParam String is_true) {
		ResultInfo<String> resultInfo = new ResultInfo<String>(ResultCode.SUCCESS);
		qService.answer(ques_id, Boolean.valueOf(is_true));
		return resultInfo;
	}
	
	@ApiOperation(value = "获取1道测试题目", notes = "获取测试题目信息", produces="application/json")
	@RequestMapping(value = "/getQuestion", method = RequestMethod.GET)
	public Question getQuestion() {
		return qService.getQuestion();
	}

}
