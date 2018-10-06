package org.sojourner.message.api;

import java.util.Map;

import org.sojourner.message.core.page.PageBean;
import org.sojourner.message.core.page.PageParam;
import org.sojourner.message.entity.TransactionMessage;
import org.sojourner.message.exception.TransactionBisException;

public interface TransactionMessageService {

	/**
	 * 消息预存
	 */
	int saveMessageWaitingConfirm(TransactionMessage message) throws TransactionBisException;
	
	/**
	 * 确认并发送消息
	 */
	void confirmAndSendMessage(String messageId) throws TransactionBisException;
	
	/**
	 * 存储并发送消息
	 */
	int saveAndSendMessage(TransactionMessage rpTransactionMessage) throws TransactionBisException;
	
	/**
	 * 重发消息.
	 */
	 void reSendMessage(TransactionMessage rpTransactionMessage) throws TransactionBisException;
	 
	 /**
	  * 根据messageId重发某条消息.
	  */
	 void reSendMessageByMessageId(String messageId) throws TransactionBisException;
	 
	 /**
	  * 将消息标记为死亡消息
	  */
	 void setMessageToAreadlyDead(String messageId) throws TransactionBisException;
	 
	 /**
	  * 根据消息ID获取消息
	  */
	 TransactionMessage getMessageByMessageId(String messageId) throws TransactionBisException;
	 
	 /**
	  * 根据消息ID删除消息
	  */
	 void deleteMessageByMessageId(String messageId) throws TransactionBisException;
	 
	 /**
	  * 重发某个消息队列中的全部已死亡的消息.
	  */
	 void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws TransactionBisException;
	 
	 /**
	  * 获取分页数据
	  */
	 PageBean<TransactionMessage> listPage(PageParam pageParam, Map<String, Object> paramMap);
	 
	 
	 
	
}
