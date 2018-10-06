package org.sojourner.message.monitor.schedule.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sojourner.message.api.TransactionMessageService;
import org.sojourner.message.core.page.PageBean;
import org.sojourner.message.core.page.PageParam;
import org.sojourner.message.entity.TransactionMessage;
import org.sojourner.message.monitor.schedule.MessageScheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("messageSchedule")
public class MessageScheduledImpl implements MessageScheduled {

	@Autowired
	private TransactionMessageService transactionMessageService;

	@Override
	public void handleWaitingConfirmTimeoutMessages() {

		// 每页数量
		int num = 2000;
		// 一次处理的页数
		int page = 3;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, TransactionMessage> messageMap = getMessageMap(num, page, paramMap);

	}

	private Map<String, TransactionMessage> getMessageMap(int num, int page, Map<String, Object> paramMap) {
		int currentPage = 1;

		Map<String, TransactionMessage> messageMap = new HashMap<>();

		PageBean<TransactionMessage> pageBean = transactionMessageService.listPage(new PageParam(num, currentPage),
				paramMap);
		List<TransactionMessage> list = pageBean.getRecordList();
		if (list == null || list.isEmpty()) {
			return messageMap;
		}

		for (TransactionMessage message : list) {
			messageMap.put(message.getMessageId(), message);
		}
		//TODO 页数配置判断
		int pageCount = pageBean.getTotalPage();

		return messageMap;
	}
}
