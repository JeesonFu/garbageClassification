package org.bistu.garbageclassification.bean.others;

import lombok.Data;

/**
 * 测试模块，每道题的选项信息
 * @author 付金振
 *
 */
@Data
public class Option {

	private Integer oid = 0;
	private String select = "";
	private boolean is_true = true;
	
	public Option() {
	}
	
	public Option(Integer oid, String select, boolean is_true) {
		this.oid = oid;
		this.select = select;
		this.is_true = is_true;
	}
}
