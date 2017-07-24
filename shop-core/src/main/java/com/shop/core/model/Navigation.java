package com.shop.core.model;

import com.shop.core.base.BaseModel;

@SuppressWarnings("serial")
public class Navigation extends BaseModel {
	
	
	private Integer orders;
	private Integer isBlankTarget;
	private String name;
	private Integer position;
	private String url;
	
	
	public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
	}
	public Integer getIsBlankTarget() {
		return isBlankTarget;
	}
	public void setIsBlankTarget(Integer isBlankTarget) {
		this.isBlankTarget = isBlankTarget;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
