package org.bistu.garbageclassification.utils;

/**
 * 请求状态码枚举
适用场景：返回请求结果（成功/失败/异常）时；捕获异常时
 * @author 付金振
 *
 */
public enum ResultCode {
	
	/* 状态码 */
	SUCCESS(200,"success"),
	FAILURE(0,"failure");
	
	/* 参数错误 */
	
	/* 用户错误 */
	
	private Integer code;
	private String msg;
	
	ResultCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return this.code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
