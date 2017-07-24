package com.shop.core.constant;
/**
 * 状态
 */
public enum OrderStatus {

	/** 等待付款 */
	pendingPayment(0),

	/** 等待审核 */
	pendingReview(1),

	/** 等待发货 */
	pendingShipment(2),

	/** 已发货 */
	shipped(3),

	/** 已收货 */
	received(4),

	/** 已完成 */
	completed(5),

	/** 已失败 */
	failed(6),

	/** 已拒绝 */
	denied(8),

	/** 已取消 */
	canceled(7);
	private int status;

	private OrderStatus() {

	}
	private OrderStatus(int status) {
		this.status = status;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}


}