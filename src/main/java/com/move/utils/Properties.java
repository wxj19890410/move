package com.move.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局参数
 * 
 * @author Administrator
 * 
 */
@Component
@ConfigurationProperties(prefix = "application")
public  class Properties {

	/**
	 * 上传文件本地路径
	 */
	public  static String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
