package org.bistu.garbageclassification.controller;

import java.util.ArrayList;
import java.util.List;

import org.bistu.garbageclassification.bean.Garbage;
import org.bistu.garbageclassification.service.GarbageService;
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

@Api(description = "GarbageController - 关于垃圾的请求")
@RequestMapping(value = "/garbage")
@RestController
public class GarbageController {

	@Autowired
	private GarbageService garbageService;
	
	/**
	 * 垃圾类别详情页：
	 * 根据所选类别，随机获取该类别的10个垃圾
	 * @return
	 */
	@ApiOperation(value = "获取垃圾列表", notes = "根据类别随机获取10个", produces="application/json")
	@ApiImplicitParam(dataType = "Integer", name = "type_id", value = "类别编号", required = true)
	@RequestMapping(value = "/gListByType", method = RequestMethod.POST) 
	public ResultInfo<Garbage> getGListByType(@RequestParam Integer type_id) {
		ResultInfo<Garbage> resultInfo = new ResultInfo<Garbage>(ResultCode.SUCCESS);
		resultInfo.setData(garbageService.getGListByType(type_id));

		return resultInfo;
	}
	
	/**
	 * 文字检索-精确查询
	 * 搜索结果页：查询并显示被搜索的垃圾详细信息
	 */
	@ApiOperation(value = "精确查询垃圾", notes = "与垃圾名称完全一致的项", produces="application/json")
	@ApiImplicitParam(dataType = "String", name = "name", value = "垃圾名称", required = true)
	@RequestMapping(value = "/exactQuery", method = RequestMethod.POST) 
	public ResultInfo<Garbage> exactQuery(@RequestParam String name) {
		ResultInfo<Garbage> resultInfo = new ResultInfo<Garbage>(ResultCode.SUCCESS);
		List<Garbage> garbage_detail = new ArrayList<Garbage>();
		garbage_detail.add(garbageService.exactQuery(name));
		resultInfo.setData(garbage_detail);
		return resultInfo;
	}
	
	/**
	 * 热门搜索榜单：获得搜索次数最多的前10个
	 */
	@ApiOperation(value = "热门搜索榜", notes = "搜索次数最多的前10个", produces="application/json")
	@RequestMapping(value = "/getTops", method = RequestMethod.GET) 
	public ResultInfo<Garbage> getTops() {
		ResultInfo<Garbage> resultInfo = new ResultInfo<Garbage>(ResultCode.SUCCESS);
		resultInfo.setData(garbageService.getTops());
		
		return resultInfo;
	}
	
	/**
	 * 搜索结果页：您要找的是不是，模糊查询
	 */
	@ApiOperation(value = "您要找的是不是", notes = "最多5或6个", produces="application/json")
	@ApiImplicitParam(dataType = "String", name = "name", value = "垃圾名称", required = true)
	@RequestMapping(value = "/getSimilar", method = RequestMethod.POST) 
	public ResultInfo<String> getSimilar(@RequestParam String name) {
		ResultInfo<String> resultInfo = new ResultInfo<String>(ResultCode.SUCCESS);
		resultInfo.setData(garbageService.getSimilar(name));
		return resultInfo;
	}
}
