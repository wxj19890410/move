package com.move.action;

import com.move.dao.DataOriginalDao;
import com.move.model.OrgDepartment;
import com.move.model.UserData;
import com.move.service.DataService;
import com.move.service.OrgService;
import com.move.service.UserService;
import com.move.service.WxDataService;
import com.move.utils.DictUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "mobile")
public class MobileAction {

	@Autowired
	private DataService dataService;

	@Autowired
	private OrgService orgService;

	@Autowired
	private WxDataService wxDataService;

	@GetMapping(value = "getInfos")
	public Object getInfos(String userid, String month, UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		// 获取本月信息
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.total>t.total and t1.month = t.month and t1.delFlag='0')",
				"rank");
		QueryUtils.addColumn(qb, "u.personNub", "personNub");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value1>t.value1 and t1.month = t.month and t1.delFlag='0')",
				"studyRank");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value2>t.value2 and t1.month = t.month and t1.delFlag='0')",
				"readRank");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value3>t.value3 and t1.month = t.month and t1.delFlag='0')",
				"cultureRank");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value4>t.value4 and t1.month = t.month and t1.delFlag='0')",
				"attendanceRank");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value5>t.value5 and t1.month = t.month and t1.delFlag='0')",
				"hseRank");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value6>t.value6 and t1.month = t.month and t1.delFlag='0')",
				"improveRank");
		QueryUtils.addColumn(qb, "t.total", "total");
		QueryUtils.addColumn(qb, "u.value1", "averStudy");
		QueryUtils.addColumn(qb, "u.value2", "averRead");
		QueryUtils.addColumn(qb, "u.value3", "averCulture");
		QueryUtils.addColumn(qb, "u.value4", "averAttendance");
		QueryUtils.addColumn(qb, "u.value5", "averHse");
		QueryUtils.addColumn(qb, "u.value6", "averImprove");
		QueryUtils.addColumn(qb, "u.value1+u.value2+u.value3+u.value4+u.value5+u.value6", "averTotal");
		QueryUtils.addWhere(qb, "and t.userid = {0}", userid);
		QueryUtils.addJoin(qb,
				"left join DataResult u on u.month = t.month and u.delFlag = '0' and u.relationType='company'");
		QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		Map<String, Object> dataOriginal = dataService.originalMap(qb);
		data.put("data", dataOriginal);
		return data;
	}

	@GetMapping(value = "myHistoryData")
	public Object myHistoryData(String userid, String dataType, UserInfo userInfo) {

		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.month", "month");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.total", "total");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and t.userid = {0}", userid);
		QueryUtils.addOrder(qb, "t.month desc");
		return dataService.originalMapList(qb);
	}

	@GetMapping(value = "orgInfo")
	public Object getOrgInfo(UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		// 部门
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addWhere(qb,
				"and exists(select t1.id from OrgRelation t1 where t1.relationId = t.id and t1.relationType='dept')");
		QueryUtils.addOrder(qb, "t.id ");
		// 组织
		data.put("dept", orgService.findDeptMap(qb));
		qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.tagid", "id");
		QueryUtils.addColumn(qb, "t.tagname", "name");
		QueryUtils.addWhere(qb,
				"and exists(select t1.id from OrgRelation t1 where t1.relationId = t.tagid and t1.relationType='tag')");
		QueryUtils.addOrder(qb, "t.tagid");
		data.put("tag", orgService.findGroupMap(qb));
		return data;
	}

	@GetMapping(value = "averData")
	public Object averData(Integer groupId, Integer deptId, String month, UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(0);
		qb.setLength(10);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.total>t.total and t1.month = t.month and t1.delFlag='0')",
				"rank");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addColumn(qb, "t.total", "total");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		if (Utilities.isValidId(deptId)) {
			QueryUtils.addWhere(qb, "and exists(from OrgRelation t1 where t1.relationId = {0} and t1.relationType = 'dept' and t1.userid=t.userid)", deptId);
		} else if (Utilities.isValidId(groupId)) {
			QueryUtils.addWhere(qb, "and exists(from OrgRelation t1 where t1.relationId = {0} and t1.relationType = 'tag' and t1.userid=t.userid)", groupId);
		}
		QueryUtils.addWhere(qb, "and t.month = {0}", month);
		QueryUtils.addOrder(qb, "t.total desc");
		return dataService.DataGrid(qb);
	}

	/***
	 * 手机登录
	 * 
	 * @param userid
	 * @return
	 */
	@GetMapping(value = "loadInfo")
	public Object loadInfo(String codeId, String userid) {
		// 获取 access_token
		return wxDataService.loadInfo(codeId, userid);
	}

}
