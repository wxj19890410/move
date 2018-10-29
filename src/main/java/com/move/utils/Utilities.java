package com.move.utils;

import com.move.model.BaseModel;
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

	/**
	 * 验证主键是否大于0
	 *
	 * @param id
	 *            主键
	 * @return 是否大于0
	 */
	public static boolean isValidId(Integer  id) {
		return id != null && id > 0;
	}

	public static void setUserInfo(BaseModel model, UserInfo userInfo) {
        Date now = new Date();
		if (userInfo != null) {
			model.setCreateUser(userInfo.getUserId());
			model.setCreateDate(now);
			model.setEditDate(now);
			if (!Utilities.isValidId(model.getId())) {
				model.setDelFlag(DictUtils.NO);
				model.setCreateDate(now);
			}
		} else {
			model.setEditDate(now);
			if (!Utilities.isValidId(model.getId())) {
				model.setDelFlag(DictUtils.NO);
				model.setCreateDate(new Date());
			}
		}
	}

	/**
	 * 对象是否相等
	 *
	 * @param obj1
	 *            对象1
	 * @param obj2
	 *            对象2
	 * @return
	 */
	public static boolean equals(Object obj1, Object obj2) {
		return obj1 != null ? obj1.equals(obj2) : obj2 == null;
	}


}
