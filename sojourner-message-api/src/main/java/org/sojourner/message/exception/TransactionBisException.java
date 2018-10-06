package org.sojourner.message.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sojourner.message.core.exception.BizException;

public class TransactionBisException extends BizException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 消息为空
	 */
	public static final int SAVA_MESSAGE_IS_NULL = 1001;
	/**
	 * 消息的消费队列为空
	 */
	public static final int MESSAGE_CONSUMER_QUEUE_IS_NULL = 1002;
	
	private static final Logger LOG = LoggerFactory.getLogger(TransactionBisException.class);
	
	public TransactionBisException() {
	}
	
	public TransactionBisException(int code, String msg) {
		super(code,msg);
	}

	

}
