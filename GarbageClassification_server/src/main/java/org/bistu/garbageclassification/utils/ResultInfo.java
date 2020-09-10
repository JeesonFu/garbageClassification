package org.bistu.garbageclassification.utils;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 返回请求结果数据体，JSON格式
 * @author 付金振
 *
 */
@Data
public class ResultInfo<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer code;
	private String msg;
	private List<T> data;
	
	//主要
	public ResultInfo(ResultCode resultCode) {
		this.code = resultCode.getCode();
		this.msg = resultCode.getMsg();
	}
	
	public ResultInfo(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public ResultInfo(ResultCode resultCode, List<T> data) {
		this.code = resultCode.getCode();
		this.msg = resultCode.getMsg();
		this.data = data;
	}
	
	public ResultInfo(Integer code, String msg, List<T> data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
}
