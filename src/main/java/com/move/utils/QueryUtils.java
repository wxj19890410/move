package com.move.utils;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class QueryUtils {
	public static final String ADD_COLUMN = " {0} as {1} ";

	public static final String ADD_DICT = " (select t1.dictValue from SysDict t1 where t1.dictKey = {0} and t1.type = '{1}') as {2} ";

	public static final String ADD_USER = " (select t1.userName from SysUser t1 where t1.id = {0}) as {1} ";

	public static QueryBuilder addColumn(QueryBuilder qb, String name) {
		int index = StringUtils.indexOf(name, ".");
		String alias = index != -1 ? StringUtils.substring(name, index + 1) : name;
		return qb.addColumn(Utilities.format(ADD_COLUMN, name, alias));
	}

	public static QueryBuilder addColumn(QueryBuilder qb, String name, String alias, Object... params) {
		return qb.addColumn(Utilities.format(ADD_COLUMN, name, alias), params);
	}

	public static QueryBuilder addDict(QueryBuilder qb, String name, String type) {
		int index = StringUtils.indexOf(name, ".");
		String alias = (index != -1 ? StringUtils.substring(name, index + 1) : name) + "Name";
		return qb.addColumn(Utilities.format(ADD_DICT, name, type, alias));
	}

	public static QueryBuilder addDict(QueryBuilder qb, String name, String type, String alias) {
		return qb.addColumn(Utilities.format(ADD_DICT, name, type, alias));
	}

	public static QueryBuilder addUser(QueryBuilder qb, String name, String alias) {
		return qb.addColumn(Utilities.format(ADD_USER, name, alias));
	}

	public static QueryBuilder addSetColumn(QueryBuilder qb, String name, Object param) {
		return qb.addColumn(name + " = {0}", param);
	}

	public static QueryBuilder addSetUserInfo(QueryBuilder qb, UserInfo userInfo) {
		qb.addColumn("t.operator = {0}", userInfo.getUserId());
		qb.addColumn("t.operateDate = {0}", userInfo.getOperateDate());
		return qb;
	}

	public static QueryBuilder addWhere(QueryBuilder qb, String str, Object... params) {
		return qb.addWhere(str, params);
	}

	public static QueryBuilder addWhereIfNotNull(QueryBuilder qb, String str, Object param) {
		if (param != null) {
			qb.addWhere(str, param);
		}

		return qb;
	}

	public static QueryBuilder addWhereIfNotEmpty(QueryBuilder qb, String str, String param) {
		if (!StringUtils.isEmpty(param)) {
			qb.addWhere(str, param);
		}

		return qb;
	}

	public static QueryBuilder addWhereIfNotEmpty(QueryBuilder qb, String str, Collection<?> param) {
		if (!CollectionUtils.isEmpty(param)) {
			qb.addWhere(str, param);
		}

		return qb;
	}

	public static QueryBuilder addOrder(QueryBuilder qb, String str) {
		return qb.addOrder(str);
	}

	public static QueryBuilder addGroup(QueryBuilder qb, String str) {
		return qb.addGroup(str);
	}

	public static QueryBuilder addHaving(QueryBuilder qb, String str, Object... params) {
		return qb.addHaving(str, params);
	}

	public static QueryBuilder addJoin(QueryBuilder qb, String str, Object... params) {
		return qb.addJoin(str, params);
	}
}
