package org.bistu.garbageclassification.entities.others;

/**
 * 题的选项信息
 */
public class Option {

	private Integer oid;
	private String select;
	private boolean is_true;
	
	public Option() {
	}
	
	public Option(Integer oid, String select, boolean is_true) {
		this.oid = oid;
		this.select = select;
		this.is_true = is_true;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public boolean isIs_true() {
		return is_true;
	}

	public void setIs_true(boolean is_true) {
		this.is_true = is_true;
	}
}
