package org.bistu.garbageclassification.controller;

import org.bistu.garbageclassification.bean.Policy;
import org.bistu.garbageclassification.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description = "生活垃圾管理条例的接口")
@RequestMapping(value = "/policy")  
@RestController
public class PolicyController {

	@Autowired
	private PolicyService policyService;
	
	@ApiOperation(value = "获取条例信息", notes = "按城市名称查询", produces="application/json") 
	@ApiImplicitParam(dataType = "string", name = "city", value = "城市名称", required = true)
	@RequestMapping(value = "/getPolicyByCity", method = RequestMethod.POST)
	public Policy getPolicyByCity(@RequestParam String city) {
		return policyService.getPolicyByCity(city);
	}
	
}
