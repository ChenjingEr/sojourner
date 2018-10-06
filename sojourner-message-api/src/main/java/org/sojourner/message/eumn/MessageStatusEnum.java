package org.sojourner.message.eumn;

/**
 * @author JING
 * @date 2018年10月5日
 * @describe message枚举状态
 */
public enum MessageStatusEnum {

	WAITING_CONFIRM("待确认"),

	SENDING("发送中");

	/** 描述 */
	private String desc;

	private MessageStatusEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
