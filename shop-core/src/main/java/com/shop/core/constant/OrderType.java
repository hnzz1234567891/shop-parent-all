package com.shop.core.constant;
/**
 * 类型
 */
public enum OrderType {

	/** 普通订单 */
	general(0),

	/** 兑换订单 */
	exchange(1);

	private OrderType() {

	}

	private OrderType(int type) {
		this.type = type;
	}

	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


}