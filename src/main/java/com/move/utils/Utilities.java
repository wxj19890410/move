package com.move.utils;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 全局帮助类
 * 
 * @author Administrator
 * 
 */
public class Utilities {
	/**
	 * 日期转换成字符串
	 *
	 * @param date
	 *            日期
	 * @param format
	 *            格式
	 * @return
	 */

	public static String formatDate(Date date, String format) {
		try {
			return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	public static String newFilePath() {
		String path =   Utilities.formatDate(new Date(), "yyyy-MM") + "/";

		if (!new File(getFilePath(path)).exists()) {
			new File(getFilePath(path)).mkdirs();
		}

		return path;
	}
	public static String newFileName() {
		return newFilePath() + UUID.randomUUID().toString();
	}

	public static String getFilePath(String path) {
		return path;
	}

}
