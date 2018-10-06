package org.sojourner.message.eumn;

public enum PublicEnum {

	YES("是"),

	NO("否");

	/** 描述 */
	private String desc;

	private PublicEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
