package org.sojourner.message.monitor;

import org.sojourner.message.monitor.schedule.MessageScheduled;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class MessageTask {

	private MessageTask() {

	}

	public static void main(String[] args) {

		try {
			@SuppressWarnings("resource")
			final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "spring-context.xml" });
			context.start();
			final MessageScheduled settScheduled = (MessageScheduled) context.getBean("messageScheduled");
			ThreadPoolTaskExecutor threadPool = (ThreadPoolTaskExecutor) context.getBean("threadPool");

			// 开一个子线程处理状态为“待确认”但已超时的消息.
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {

						settScheduled.handleWaitingConfirmTimeOutMessages();
						
						try {
							Thread.sleep(60000);
						} catch (InterruptedException e) {
						}
					}
				}
			});

			// 开一个子线程处理状态为“发送中”但超时没有被成功消费确认的消息
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						settScheduled.handleSendingTimeOutMessage();
						try {
							Thread.sleep(60000);
						} catch (InterruptedException e) {
						}
					}
				}
			});

		} catch (Exception e) {
		}
	}
}

