package org.sojourner.message.dao.impl;

import org.sojourner.message.core.dao.impl.BaseDaoImpl;
import org.sojourner.message.dao.TransactionMessageDao;
import org.sojourner.message.entity.TransactionMessage;
import org.springframework.stereotype.Repository;

@Repository("transactionMessageDao")
public class TransactionMessageDaoImpl extends BaseDaoImpl<TransactionMessage> implements TransactionMessageDao {
}
