package org.sojourner.message.monitor.schedule;

public interface MessageScheduled {

	/**
	 * 处理状态为“待确认”但已超时的消息.
	 */	
	void handleWaitingConfirmTimeOutMessages();
	
	/**
	 * 处理状态为“发送中”但超时没有被成功消费确认的消息
	 */
	void handleSendingTimeOutMessage();
}
