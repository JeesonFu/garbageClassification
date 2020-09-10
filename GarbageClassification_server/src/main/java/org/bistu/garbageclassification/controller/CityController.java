package org.bistu.garbageclassification.controller;

import org.bistu.garbageclassification.bean.City;
import org.bistu.garbageclassification.service.CityService;
import org.bistu.garbageclassification.utils.ResultCode;
import org.bistu.garbageclassification.utils.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 城市列表
 * @author 付金振
 *
 */
@Api(description = "CityController - 关于城市的请求")
@RequestMapping(value = "/city")
@RestController
public class CityController {

	@Autowired
	private CityService cityService;

	
	/**
	 * 城市切换页：获取城市列表
	 * @return
	 */
	@ApiOperation(value = "获取城市列表", notes = "城市切换页", produces="application/json")
	@RequestMapping(value = "/getCityList", method = RequestMethod.GET)
	public ResultInfo<City> getCityList() {
		ResultInfo<City> resultInfo = new ResultInfo<City>(ResultCode.SUCCESS);
		resultInfo.setData(cityService.getAllCity());
		
		return resultInfo;
	}
	
}
