package com.move.utils;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate.Param;
import com.google.common.collect.Lists;
import com.move.model.BaseModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 全局帮助类
 * 
 * @author Administrator
 * 
 */
public class Utilities {
	public static String format(String str, Object... args) {
		return MessageFormat.format(str.replaceAll("'", "''"), args);
	}

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
		String path = Utilities.formatDate(new Date(), "yyyy-MM") + "/";

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
	public static boolean isValidId(Integer id) {
		return id != null && id > 0;
	}

	public static void setUserInfo(BaseModel model, UserInfo userInfo) {
		Date now = new Date();
		if (userInfo != null) {
			model.setCreateUser(userInfo.getUserId());
			model.setCreateDate(now);
			// model.setEditDate(now);
			if (!Utilities.isValidId(model.getId())) {
				model.setDelFlag(DictUtils.NO);
				// model.setEditDate(now);
			}
		} else {
			// model.setEditDate(now);
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

	/**
	 * 分隔字符串
	 *
	 * @param str
	 *            字符串
	 * @param separator
	 *            分隔符
	 * @return
	 */
	public static List<String> split(String str, String separator) {
		List<String> result = Lists.newArrayList();

		if (!StringUtils.isEmpty(str)) {
			for (String value : StringUtils.split(str, separator)) {
				if (!StringUtils.isEmpty(value)) {
					result.add(value);
				}
			}
		}

		return result;
	}

	/**
	 * 增加天数 小时 分钟
	 *
	 * @param date
	 * @param day
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Date addDayHourMinute(Date date, Integer day, Integer hour, Integer minute) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DATE, Utilities.ifNull(day));
		ca.add(Calendar.HOUR, Utilities.ifNull(hour));
		ca.add(Calendar.MINUTE, Utilities.ifNull(minute));
		return ca.getTime();
	}

	/**
	 * 如果为空值返回0
	 *
	 * @param n
	 *            整数
	 * @return
	 */
	public static Integer ifNull(Integer n) {
		return n != null ? n : 0;
	}

	public static List<String> setMonthList(String startMonth, Integer monthNub) {
		List<String> date = Lists.newArrayList();
		date.add(startMonth);
		if (null != monthNub && monthNub > 0) {
			Integer year = Integer.parseInt(startMonth);
			for (int t = 0; t < monthNub; t++) {
				Integer year2 = year + t + 1;
				date.add(year2.toString());
			}
		}
		return date;
	}

	public static List<String> setMonthList2(String startMonth, Integer monthNub) {
		List<String> date = Lists.newArrayList();
		date.add(startMonth);
		if (null != monthNub && monthNub > 0) {
			List<String> data = split(startMonth, "-");
			if (data.size() > 1) {
				Integer year = Integer.parseInt(data.get(0));
				Integer month = Integer.parseInt(data.get(1));
				for (int t = 0; t < monthNub; t++) {
					if (equals(month, 12)) {
						month = 1;
						year = year + 1;
					} else {
						month = month + 1;
					}
					date.add(year + "-" + month);
				}
			}
		}
		return date;
	}

	public static UserInfo getUserInfo(String loginUuid) {
		return Globals.USER_INFOS.get(loginUuid);
	}

}
