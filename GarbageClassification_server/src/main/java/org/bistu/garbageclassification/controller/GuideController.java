package org.bistu.garbageclassification.controller;

import org.bistu.garbageclassification.bean.Guide;
import org.bistu.garbageclassification.service.GuideService;
import org.bistu.garbageclassification.utils.ResultCode;
import org.bistu.garbageclassification.utils.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description = "专题指南的接口")
@RequestMapping(value = "/guide")  
@RestController
public class GuideController {

	@Autowired
	private GuideService guideService;
	
	@ApiOperation(value = "获取专题指南列表", notes = "随机获取5个标题", produces="application/json")
	@RequestMapping(value = "/getGuides", method = RequestMethod.GET)
	public ResultInfo<Guide> getGuides() {
		ResultInfo<Guide> resultInfo = new ResultInfo<Guide>(ResultCode.SUCCESS);
		resultInfo.setData(guideService.getGuides());
		return resultInfo;
	}
	
	@ApiOperation(value = "获取专题指南详情", notes = "根据id获取详情", produces="application/json")
	@ApiImplicitParam(dataType = "int", name = "guide_id", value = "指南id", required = true)
	@RequestMapping(value = "/getGuide", method = RequestMethod.POST) 
	public Guide getGuide(@RequestParam Integer guide_id) {
		return guideService.getGuide(guide_id);
	}
	
}
