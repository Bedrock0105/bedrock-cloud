package org.bedrock.ai.enums;

import lombok.Getter;

@Getter
public enum SkillNodeType {
	/**
	 * 文件
	 */
	FILE("FILE", "文件"),
	/**
	 * 文件夹
	 */
	DIRECTORY("DIRECTORY", "文件夹");

	/**
	 * 编码
	 */
	private final String code;

	/**
	 * 描述
	 */
	private final String description;

	SkillNodeType(String code, String description) {
		this.code = code;
		this.description = description;
	}
}
