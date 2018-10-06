package org.sojourner.message.monitor;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class MessageTask {

	public static void main(String[] args) {
		final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"spring-context.xml"});
		applicationContext.start();
		
		ThreadPoolTaskExecutor threadPool = (ThreadPoolTaskExecutor) applicationContext.getBean("messageSchedual");
		
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				
				while(true) {
					//TODO 确认消息状态,根据状态发送
				}
			}
		});
		
	}
}
