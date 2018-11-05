package com.move.action;

import com.move.dao.DataOriginalDao;
import com.move.model.OrgDepartment;
import com.move.model.UserData;
import com.move.service.DataService;
import com.move.service.OrgService;
import com.move.service.UserService;
import com.move.utils.DictUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "mobile")
public class MobileAction {

	@Autowired
	private DataService dataService;

	@Autowired
	private UserService userService;

	@GetMapping(value = "getHistoryData")
	public Object findOriginal(String openId, UserInfo userInfo) {
		return openId;
	}

	@GetMapping(value = "getInfos")
	public Object getInfos(String openId,String month, UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		// 个人信息
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and t.openId = {0}", openId);
		List<UserData> userData = userService.findUsers(qb);
		data.put("info", userData.get(0));
		//获取本月信息
		qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "(select count(t1.id)+1 from DataOriginal t1 where t1.total>t.total and t1.month = t.month and t1.delFlag='0')","rank");
		QueryUtils.addColumn(qb, "u.personNub","personNub");
		QueryUtils.addColumn(qb, "t.value1","study");
		QueryUtils.addColumn(qb, "(select count(t1.id)+1 from DataOriginal t1 where t1.value1>t.value1 and t1.month = t.month and t1.delFlag='0')","studyRank");
		QueryUtils.addColumn(qb, "t.value2","read");
		QueryUtils.addColumn(qb, "(select count(t1.id)+1 from DataOriginal t1 where t1.value2>t.value2 and t1.month = t.month and t1.delFlag='0')","readRank");
		QueryUtils.addColumn(qb, "t.value3","culture");
		QueryUtils.addColumn(qb, "(select count(t1.id)+1 from DataOriginal t1 where t1.value3>t.value3 and t1.month = t.month and t1.delFlag='0')","cultureRank");
		QueryUtils.addColumn(qb, "t.value4","attendance");
		QueryUtils.addColumn(qb, "(select count(t1.id)+1 from DataOriginal t1 where t1.value4>t.value4 and t1.month = t.month and t1.delFlag='0')","attendanceRank");
		QueryUtils.addColumn(qb, "t.value5","hse");
		QueryUtils.addColumn(qb, "(select count(t1.id)+1 from DataOriginal t1 where t1.value5>t.value5 and t1.month = t.month and t1.delFlag='0')","hseRank");
		QueryUtils.addColumn(qb, "t.value6","improve");
		QueryUtils.addColumn(qb, "(select count(t1.id)+1 from DataOriginal t1 where t1.value6>t.value6 and t1.month = t.month and t1.delFlag='0')","improveRank");
		QueryUtils.addColumn(qb, "t.total","total");
		QueryUtils.addColumn(qb, "u.value1","averStudy");
		QueryUtils.addColumn(qb, "u.value2","averRead");
		QueryUtils.addColumn(qb, "u.value3","averCulture");
		QueryUtils.addColumn(qb, "u.value4","averAttendance");
		QueryUtils.addColumn(qb, "u.value5","averHse");
		QueryUtils.addColumn(qb, "u.value6","averImprove");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and t.openId = {0}", openId);
		QueryUtils.addJoin(qb, "left join DataResult u on u.month = t.month and u.delFlag = '0' and u.relationType='company'");
		QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		Map<String,Object> dataOriginal = dataService.originalMap(qb);
		data.put("data", dataOriginal);
		return data;
	}
	@GetMapping(value = "myHistoryData")
	public Object myHistoryData(String openId,String dataType,UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.month","month");
		QueryUtils.addColumn(qb, "t.value1","study");
		QueryUtils.addColumn(qb, "t.value2","read");
		QueryUtils.addColumn(qb, "t.value3","culture");
		QueryUtils.addColumn(qb, "t.value4","attendance");
		QueryUtils.addColumn(qb, "t.value5","hse");
		QueryUtils.addColumn(qb, "t.value6","improve");
		QueryUtils.addColumn(qb, "t.total","total");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and t.openId = {0}", openId);
		return dataService.originalMapList(qb);
	}
	@GetMapping(value = "historyData")
	public Object historyData(Integer groupId,Integer deptId,String month, UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(0);
		qb.setLength(10);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "user.name","userName");
		QueryUtils.addColumn(qb, "t.total","total");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhereIfNotNull(qb, "and t.groupId = {0}", groupId);
		QueryUtils.addWhereIfNotNull(qb, "and u.deptId = {0}", deptId);
		QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		QueryUtils.addJoin(qb, "left join OrgRelation u on u.openId = t.openId and u.delFlag = '0'");
		QueryUtils.addJoin(qb, "left join UserData user on user.openId = t.openId and u.delFlag = '0'");
		QueryUtils.addOrder(qb, "t.total desc");
		return dataService.DataGrid(qb);
	}

}
