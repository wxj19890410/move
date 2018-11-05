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
	
	/**
	 * 企业id
	 */
	public  static String corpId;
	
	/**
	 * 应用密匙
	 */
	public  static String corpSecret;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public static String getCorpId() {
		return corpId;
	}

	public static void setCorpId(String corpId) {
		Properties.corpId = corpId;
	}

	public static String getCorpSecret() {
		return corpSecret;
	}

	public static void setCorpSecret(String corpSecret) {
		Properties.corpSecret = corpSecret;
	}

	
	
}
