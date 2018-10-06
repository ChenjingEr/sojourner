package org.sojourner.message.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.sojourner.message.core.page.PageBean;
import org.sojourner.message.core.page.PageParam;
import org.sojourner.message.core.util.StringUtil;
import org.sojourner.message.dao.TransactionMessageDao;
import org.sojourner.message.entity.TransactionMessage;
import org.sojourner.message.eumn.MessageStatusEnum;
import org.sojourner.message.eumn.PublicEnum;
import org.sojourner.message.exception.TransactionBisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

/**
 * @author JING
 * @date 2018年10月5日
 * @describe 可靠消息服务
 */

@Service("transactionMessageService")
public class TransactionMessageServiceImpl implements TransactionMessageService {

	@Autowired
	private TransactionMessageDao transactionMessageDao;

	@Autowired
	private JmsTemplate jmsTemplate;

	public int saveMessageWaitingConfirm(TransactionMessage message) throws TransactionBisException {
		if (message == null) {
			throw new TransactionBisException(TransactionBisException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}

		if (StringUtil.isEmpty(message.getConsumerQueue())) {
			throw new TransactionBisException(TransactionBisException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}

		message.setEditTime(new Date());
		message.setStatus(MessageStatusEnum.WAITING_CONFIRM.name());
		message.setAreadlyDead(PublicEnum.NO.name());
		message.setMessageSendTimes(0);
		return transactionMessageDao.insert(message);
	}

	public void confirmAndSendMessage(String messageId) throws TransactionBisException {

		final TransactionMessage message = getMessageByMessageId(messageId);
		if (message == null) {
			throw new TransactionBisException(TransactionBisException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
		}

		message.setStatus(MessageStatusEnum.SENDING.name());
		message.setEditTime(new Date());
		transactionMessageDao.update(message);

		jmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message.getMessageBody());
			}
		});

	}

	public int saveAndSendMessage(final TransactionMessage message) throws TransactionBisException {
		if (message == null) {
			throw new TransactionBisException(TransactionBisException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}

		if (StringUtil.isEmpty(message.getConsumerQueue())) {
			throw new TransactionBisException(TransactionBisException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}

		message.setStatus(MessageStatusEnum.SENDING.name());
		message.setAreadlyDead(PublicEnum.NO.name());
		message.setMessageSendTimes(0);
		message.setEditTime(new Date());
		int result = transactionMessageDao.insert(message);

		jmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message.getMessageBody());
			}
		});
		
		return result;
	}

	public void reSendMessage(final TransactionMessage message) throws TransactionBisException {
		if (message == null) {
			throw new TransactionBisException(TransactionBisException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}
		
		if (StringUtil.isEmpty(message.getConsumerQueue())) {
			throw new TransactionBisException(TransactionBisException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}
		
		message.addSendTimes();
		message.setEditTime(new Date());
		transactionMessageDao.update(message);

		jmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message.getMessageBody());
			}
		});
	}

	public void reSendMessageByMessageId(String messageId) throws TransactionBisException {
		final TransactionMessage message = getMessageByMessageId(messageId);
		if (message == null) {
			throw new TransactionBisException(TransactionBisException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
		}
		
		//TODO maxTimes 可以配置
		int maxTimes = Integer.valueOf(3);
		if (message.getMessageSendTimes() >= maxTimes) {
			message.setAreadlyDead(PublicEnum.YES.name());
		}
		
		message.setEditTime(new Date());
		message.setMessageSendTimes(message.getMessageSendTimes() + 1);
		transactionMessageDao.update(message);
		
		jmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message.getMessageBody());
			}
		});
	}

	public void setMessageToAreadlyDead(String messageId) throws TransactionBisException {
		TransactionMessage message = getMessageByMessageId(messageId);
		if (message == null) {
			throw new TransactionBisException(TransactionBisException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
		}
		
		message.setAreadlyDead(PublicEnum.YES.name());
		message.setEditTime(new Date());
		transactionMessageDao.update(message);
	}

	public TransactionMessage getMessageByMessageId(String messageId) throws TransactionBisException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("messageId", messageId);

		return transactionMessageDao.getBy(paramMap);
	}

	public void deleteMessageByMessageId(String messageId) throws TransactionBisException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("messageId", messageId);
		transactionMessageDao.delete(paramMap);
	}

	public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws TransactionBisException {
		int numPerPage = 1000;
		if (batchSize > 0 && batchSize < 100){
			numPerPage = 100;
		} else if (batchSize > 100 && batchSize < 5000){
			numPerPage = batchSize;
		} else if (batchSize > 5000){
			numPerPage = 5000;
		} else {
			numPerPage = 1000;
		}
		
		int pageNum = 1;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("consumerQueue", queueName);
		paramMap.put("areadlyDead", PublicEnum.YES.name());
		paramMap.put("listPageSortType", "ASC");
		
		
		Map<String, TransactionMessage> messageMap = new HashMap<String, TransactionMessage>();
		List<Object> recordList = new ArrayList<Object>();
		int pageCount = 1;
		
		PageBean pageBean = transactionMessageDao.listPage(new PageParam(pageNum, numPerPage), paramMap);
		recordList = pageBean.getRecordList();
		if (recordList == null || recordList.isEmpty()) {
			return;
		}
		pageCount = pageBean.getTotalPage();
		for (final Object obj : recordList) {
			final TransactionMessage message = (TransactionMessage) obj;
			messageMap.put(message.getMessageId(), message);
		}

		for (pageNum = 2; pageNum <= pageCount; pageNum++) {
			pageBean = transactionMessageDao.listPage(new PageParam(pageNum, numPerPage), paramMap);
			recordList = pageBean.getRecordList();

			if (recordList == null || recordList.isEmpty()) {
				break;
			}
			
			for (final Object obj : recordList) {
				final TransactionMessage message = (TransactionMessage) obj;
				messageMap.put(message.getMessageId(), message);
			}
		}
		
		recordList = null;
		pageBean = null;
		
		for (Map.Entry<String, TransactionMessage> entry : messageMap.entrySet()) {
			final TransactionMessage message = entry.getValue();
			
			message.setEditTime(new Date());
			message.setMessageSendTimes(message.getMessageSendTimes() + 1);
			transactionMessageDao.update(message);
			
			jmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
			jmsTemplate.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(message.getMessageBody());
				}
			});
		}
	}

	public PageBean<TransactionMessage> listPage(PageParam pageParam, Map<String, Object> paramMap) {
		return transactionMessageDao.listPage(pageParam, paramMap);
	}

}
