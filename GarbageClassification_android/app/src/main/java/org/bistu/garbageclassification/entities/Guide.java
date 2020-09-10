package org.bistu.garbageclassification.entities;

/**
 * 专题指南
 */
public class Guide {

	private Integer guide_id;
	
	private String title;
	
	private String content;
	
	public Guide() {
	}
	
	public Guide(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public Guide(Integer gid, String title, String content) {
		this.guide_id = gid;
		this.title = title;
		this.content = content;
	}

	public Integer getGuide_id() {
		return guide_id;
	}

	public void setGuide_id(Integer guide_id) {
		this.guide_id = guide_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
