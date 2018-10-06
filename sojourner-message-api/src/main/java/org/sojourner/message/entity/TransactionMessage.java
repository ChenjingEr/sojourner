package org.sojourner.message.entity;

import java.util.Date;

import org.sojourner.message.core.entity.BaseEntity;

/**
 * 
 * @author JING
 * @date 2018年10月5日
 * @describe 持久化消息
 */
public class TransactionMessage extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键ID
	 */
	private Integer id;
	/**
	 * 版本号
	 */
	private Integer version;
	/**
	 * 修改者
	 */
	private String editor;
	/**
	 * 创建者
	 */
	private String creator;
	/**
	 * 最后修改时间
	 */
	private Date editTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * message_id
	 */
	private String messageId;
	/**
	 * 消息内容
	 */
	private String messageBody;
	/**
	 * 消息数据类型
	 */
	private String messageDataType;
	/**
	 * 消费队列
	 */
	private String consumerQueue;
	/**
	 * 消息重发次数
	 */
	private Integer messageSendTimes;
	/**
	 * 是否死亡
	 */
	private String areadlyDead;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 备注
	 */
	private String remark;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getMessageDataType() {
		return messageDataType;
	}
	public void setMessageDataType(String messageDataType) {
		this.messageDataType = messageDataType;
	}
	public String getConsumerQueue() {
		return consumerQueue;
	}
	public void setConsumerQueue(String consumerQueue) {
		this.consumerQueue = consumerQueue;
	}
	public Integer getMessageSendTimes() {
		return messageSendTimes;
	}
	
	public void setMessageSendTimes(Integer messageSendTimes) {
		this.messageSendTimes = messageSendTimes;
	}
	public String getAreadlyDead() {
		return areadlyDead;
	}
	public void setAreadlyDead(String areadlyDead) {
		this.areadlyDead = areadlyDead;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public void addSendTimes() {
		this.messageSendTimes += 1;
	}
	
}
