package com.meitianhui.schedule.task;

public interface OrderTask {

	
	/**
	 * 我要批超时订单自动取消
	 */
	public void fgOrderAutoCancel();
	
	
	/**
	 * 我要批超时订单自动取消
	 */
	public void proPsGroupOrder();
	

	/**
	 * 我要批超时订单自动确认收货
	 */
	public void psOrderAutoReceived();

	/**
	 * 精选特卖超时订单自动确认收货
	 */
	public void pcOrderAutoReceived();
	
	/**
	 * 伙拼团验证是否成团
	 */
	public void tsActivityCheck();

	/**
	 * 伙拼团订单超时自动收货
	 */
	public void tsOrderAutoReceived();
	
	/**
	 * 处理过期任务
	 */
	public void timeOutOdTaskAutoClosed();
	
}
