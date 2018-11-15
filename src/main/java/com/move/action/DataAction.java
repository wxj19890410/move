package com.move.action;

import com.move.service.DataService;
import com.move.service.OrgService;
import com.move.utils.DictUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "data")
public class DataAction {

	@Autowired
	private DataService dataService;

	@GetMapping(value = "originalMap")
	public Object originalMap(String openId, String month, Integer fileId, UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.month", "month");
		QueryUtils.addColumn(qb, "t.value1", "value1");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhereIfNotNull(qb, "and t.openId = {0}", openId);
		QueryUtils.addWhereIfNotNull(qb, "and t.fileId = {0}", fileId);
		QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		return dataService.originalMapList(qb);
	}

	@GetMapping(value = "dataDataGrid")
	public Object dataDataGrid(UserInfo userInfo, Integer start, Integer length, String userid, String startMonth,
			Integer monthNub, Integer fileId, String inputSearch, String mobile, String userName) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.userid", "userid");
		QueryUtils.addColumn(qb, "t.mobile", "mobile");
		QueryUtils.addColumn(qb, "t.name", "userName");
		QueryUtils.addColumn(qb, "t.month", "month");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.total", "total");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		if (StringUtils.isNotBlank(userid)) {
			QueryUtils.addWhere(qb, "and t.userid like {0}", "%" + userid + "%");
		}

		if (StringUtils.isNotBlank(userName)) {
			QueryUtils.addWhere(qb, "and t.name like {0}", "%" + userName + "%");
		}
		if (StringUtils.isNotBlank(mobile)) {
			QueryUtils.addWhere(qb, "and t.mobile like {0}", "%" + mobile + "%");
		}
		QueryUtils.addWhereIfNotNull(qb, "and t.fileId = {0}", fileId);
		if (StringUtils.isNotBlank(startMonth)) {
			List<String> months = Utilities.setMonthList(startMonth, monthNub);
			QueryUtils.addWhere(qb, "and t.month in {0}", months);
		}
		if (StringUtils.isNotBlank(inputSearch)) {
			QueryUtils.addWhere(qb, "and t.name like {0}", "%" + inputSearch + "%");
		}
		QueryUtils.addOrder(qb, "t.month desc");
		return dataService.DataGrid(qb);
	}

	@GetMapping(value = "dataResultGrid")
	public Object dataResultGrid(UserInfo userInfo, Integer start, Integer length, String startMonth, Integer monthNub,
			String relationType) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.month", "month");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.total", "total");
		QueryUtils.addColumn(qb, "t.personNub", "personNub");
		QueryUtils.addWhere(qb, "and t.personNub > 0");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addColumn(qb, "t.month", "month");
		QueryUtils.addColumn(qb, "t.createDate", "createDate");
		if (StringUtils.isNotBlank(relationType)) {
			QueryUtils.addWhere(qb, "and t.relationType = {0}", relationType);
		}

		QueryUtils.addOrder(qb, "t.month desc");
		if (StringUtils.isNotBlank(startMonth)) {
			List<String> months = Utilities.setMonthList(startMonth, monthNub);
			QueryUtils.addWhere(qb, "and t.month in {0}", months);
		}
		return dataService.DataResultGrid(qb);
	}

	@GetMapping(value = "fileDatagrid")
	public Object fileDatagrid(UserInfo userInfo, Integer start, Integer length) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.month", "month");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addColumn(qb, "t.createDate", "createDate");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addOrder(qb, "t.id desc");
		return dataService.sysFlieGrid(qb);
	}

	@GetMapping(value = "setAverageData")
	public Object setAverageData(String month, Integer fileId, UserInfo userInfo) {
		return dataService.setAverageData(month, fileId, userInfo);
	}

	@GetMapping(value = "msgHistoryDatagrid")
	public Object msgHistoryDatagrid(UserInfo userInfo, Integer start, Integer length, String inputSearch,
			String mobile, String username, String userid, String startMonth, Integer monthNub) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.userid", "userid");
		QueryUtils.addColumn(qb, "t.month", "month");
		QueryUtils.addColumn(qb, "u.name", "name");
		QueryUtils.addColumn(qb, "u.mobile", "mobile");
		QueryUtils.addColumn(qb, "t.createName", "createName");
		QueryUtils.addColumn(qb, "t.month", "month");
		QueryUtils.addColumn(qb, "t.content", "content");
		QueryUtils.addColumn(qb, "'已发送'", "state");
		QueryUtils.addColumn(qb, "t.createDate", "createDate");
		if (StringUtils.isNotBlank(startMonth)) {
			List<String> months = Utilities.setMonthList(startMonth, monthNub);
			QueryUtils.addWhereIfNotNull(qb, "and t.month in {0}", months);
		}
		if (StringUtils.isNotBlank(mobile)) {
			QueryUtils.addWhereIfNotNull(qb, "and u.mobile like {0} ", "%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(username)) {
			QueryUtils.addWhereIfNotNull(qb, "and u.name like {0}", "%" + username + "%");
		}
		if (StringUtils.isNotBlank(userid)) {
			QueryUtils.addWhereIfNotNull(qb, "and u.userid like {0}", "%" + userid + "%");
		}
		QueryUtils.addJoin(qb, "left join UserData u on u.userid = t.userid");
		QueryUtils.addWhere(qb, "and u.id is not null");
		QueryUtils.addOrder(qb, "t.id desc");
		return dataService.msgHistoryDatagrid(qb);
	}

	@GetMapping(value = "originalUpdate")
	public Object originalUpdate(Integer id, Integer value1, Integer value2, Integer value3, Integer value4,
			Integer value5, Integer value6, UserInfo userInfo) {
		return dataService.originalUpdate(id, value1, value2, value3, value4, value5, value6, userInfo);
	}
}
