package org.sojourner.message.monitor.biz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.sojourner.message.api.TransactionMessageService;
import org.sojourner.message.core.util.PublicConfigUtil;
import org.sojourner.message.entity.TransactionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author JING
 * @date 2018年10月7日
 * @describe 业务处理
 */

@Component("messageBiz")
public class MessageBiz {

	@Autowired
	private TransactionMessageService transactionMessageService;

	public void handleWaitingConfirmTimeOutMessages(Map<String, TransactionMessage> messageMap) {
		// 单条消息处理（目前该状态的消息，消费队列全部是accounting，如果后期有业务扩充，需做队列判断，做对应的业务处理。）
		for (Map.Entry<String, TransactionMessage> entry : messageMap.entrySet()) {
			TransactionMessage message = entry.getValue();
			try {
				// 插入业务 if else 状态判断
				// 确认并发送消息
				transactionMessageService.confirmAndSendMessage(message.getMessageId());

				// 已经消费，可以直接删除数据
				transactionMessageService.deleteMessageByMessageId(message.getMessageId());

			} catch (Exception e) {
			}
		}
	}

	/**
	 * 处理[SENDING]状态的消息
	 * 
	 * @param messages
	 */
	public void handleSendingTimeOutMessage(Map<String, TransactionMessage> messageMap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 根据配置获取通知间隔时间
		Map<Integer, Integer> notifyParam = getSendTime();

		// 单条消息处理
		for (Map.Entry<String, TransactionMessage> entry : messageMap.entrySet()) {
			TransactionMessage message = entry.getValue();
			try {
				// 判断发送次数
				int maxTimes = Integer.valueOf(PublicConfigUtil.readConfig("message.max.send.times"));

				// 如果超过最大发送次数直接退出
				if (maxTimes < message.getMessageSendTimes()) {
					// 标记为死亡
					transactionMessageService.setMessageToAreadlyDead(message.getMessageId());
					continue;
				}
				// 判断是否达到发送消息的时间间隔条件
				int reSendTimes = message.getMessageSendTimes();
				int times = notifyParam.get(reSendTimes == 0 ? 1 : reSendTimes);
				long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
				long needTime = currentTimeInMillis - times * 60 * 1000;
				long hasTime = message.getEditTime().getTime();
				// 判断是否达到了可以再次发送的时间条件
				if (hasTime > needTime) {
					continue;
				}

				// 重新发送消息
				transactionMessageService.reSendMessage(message);

			} catch (Exception e) {
			}
		}

	}

	/**
	 * 根据配置获取通知间隔时间
	 * 
	 * @return
	 */
	private Map<Integer, Integer> getSendTime() {
		Map<Integer, Integer> notifyParam = new HashMap<Integer, Integer>();
		notifyParam.put(1, Integer.valueOf(PublicConfigUtil.readConfig("message.send.1.time")));
		notifyParam.put(2, Integer.valueOf(PublicConfigUtil.readConfig("message.send.2.time")));
		notifyParam.put(3, Integer.valueOf(PublicConfigUtil.readConfig("message.send.3.time")));
		notifyParam.put(4, Integer.valueOf(PublicConfigUtil.readConfig("message.send.4.time")));
		notifyParam.put(5, Integer.valueOf(PublicConfigUtil.readConfig("message.send.5.time")));
		return notifyParam;
	}

}
