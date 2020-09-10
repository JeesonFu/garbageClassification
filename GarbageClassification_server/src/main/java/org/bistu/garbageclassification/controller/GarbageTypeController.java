package org.bistu.garbageclassification.controller;

import java.util.ArrayList;
import java.util.List;

import org.bistu.garbageclassification.bean.GarbageType;
import org.bistu.garbageclassification.service.GarbageTypeService;
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

@Api(description = "GarbageTypeController - 关于垃圾类别的请求")
@RequestMapping(value = "/type")
@RestController
public class GarbageTypeController {
	
	@Autowired
	private GarbageTypeService gtService;

	/*
	 * 首页：请求city对应的城市类别预览图
	 */
	@ApiOperation(value = "获取首页下方的四项图标", notes = "根据当前城市获取4个类别预览信息", produces="application/json")
	@ApiImplicitParam(dataType = "string", name = "city", value = "城市名称", required = true)
	@RequestMapping(value = "/typePre", method = RequestMethod.POST)
	public ResultInfo<GarbageType> getTypePreview(@RequestParam String city) {
		ResultInfo<GarbageType> resultInfo = new ResultInfo<GarbageType>(ResultCode.SUCCESS);
		resultInfo.setData(gtService.getTypesPre(city));
		
		return resultInfo;
	}
	
	/*
	 * 结果页、类别详情页：请求类别的详细信息
	 */
	@ApiOperation(value = "获取类别详情", notes = "类别的详情", produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "Integer", name = "type_id", value = "类别id", required = true),
		@ApiImplicitParam(dataType = "string", name = "city", value = "城市名称", required = true)
	})
	@RequestMapping(value = "/getTypeDetail", method = RequestMethod.POST)  
	public ResultInfo<GarbageType> getTypeDetail(@RequestParam Integer type_id, @RequestParam String city) {
		ResultInfo<GarbageType> resultInfo = new ResultInfo<GarbageType>(ResultCode.SUCCESS);
		List<GarbageType> detail = new ArrayList<GarbageType>();
		detail.add(gtService.getTypeDetail(type_id, city));
		resultInfo.setData(detail);
		
		return resultInfo;
	}
	
}
