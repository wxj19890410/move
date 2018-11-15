package com.move.action;

import com.move.model.UserData;
import com.move.service.LoginService;
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
@RequestMapping(value = "org")
public class OrgAction {

	@Autowired
	private OrgService orgService;

	@PostMapping(value = "saveGroup")
	public Object saveGroup(Integer id, String name, UserInfo userInfo) {
		return orgService.saveGroup(id, name, userInfo);
	}

	@PostMapping(value = "deleteGroup")
	public Object deleteGroup(Integer id, UserInfo userInfo) {
		orgService.deleteGroup(id, userInfo);
		return "success";
	}

	@GetMapping(value = "findGroup")
	public Object findGroupAll(UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);

		return orgService.findGroup(qb);
	}

	@PostMapping(value = "saveDept")
	public Object saveDept(Integer id, String name, String deptType, UserInfo userInfo) {
		return orgService.saveDept(id, name, deptType, userInfo);
	}

	@PostMapping(value = "deleteDept")
	public Object deleteDept(Integer id, UserInfo userInfo) {
		orgService.deleteDept(id, userInfo);
		return id;
	}

	@GetMapping(value = "findDept")
	public Object findDept(UserInfo userInfo, String deptType) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id", "id");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		return orgService.findDept(qb);
	}

	@GetMapping(value = "findGroupMap")
	public Object findGroupMap(UserInfo userInfo, String havaIgnore) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.tagid", "tagid");
		QueryUtils.addColumn(qb, "t.tagname", "name");
		if (StringUtils.equals(havaIgnore, DictUtils.YES)) {

		} else {
			QueryUtils.addWhere(qb,
					" and not exists(from IgnoreGroups u where u.tagid = t.tagid and u.ignoreFlag = '1')");
		}

		return orgService.findGroupMap(qb);
	}

	@GetMapping(value = "findDeptMap")
	public Object findDeptMap(UserInfo userInfo, String deptType) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.name", "name");
		if (StringUtils.isNotBlank(deptType)) {
			if (StringUtils.equals(deptType, "01")) {
				QueryUtils.addWhere(qb,
						"and not exists(from DeptRelation t1 where t1.deptId = t.id and t1.deptType ='02')");
			} else {
				QueryUtils.addWhere(qb,
						"and exists(from DeptRelation t1 where t1.deptId = t.id and t1.deptType ='02')");
			}
		}
		return orgService.findDeptMap(qb);
	}

	@GetMapping(value = "groupDataGrid")
	public Object groupDataGrid(UserInfo userInfo, Integer start, Integer length, String avg, String month,
			String inputSearch, Integer tagid, String startMonth, Integer monthNub) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.tagid");
		QueryUtils.addColumn(qb, "t.tagname", "name");
		QueryUtils.addColumn(qb, "u.ignoreFlag", "ignoreFlag");
		QueryUtils.addColumn(qb,
				"(select count(t1.id) from OrgRelation t1 where t1.relationId = t.tagid and t1.relationType = 'tag')",
				"number");
		if (StringUtils.equals(avg, "0")) {

		} else {
			QueryUtils.addWhere(qb,
					" and not exists(from IgnoreGroups u where u.tagid = t.tagid and u.ignoreFlag = '1')");
			if (StringUtils.isNotBlank(startMonth)) {
				List<String> months = Utilities.setMonthList(startMonth, monthNub);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value1)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'tag' and relationId = t.tagid)",
						"study", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value2)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'tag' and relationId = t.tagid)",
						"read", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value3)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'tag' and relationId = t.tagid)",
						"culture", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value4)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'tag' and relationId = t.tagid)",
						"attendance", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value5)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'tag' and relationId = t.tagid)",
						"hse", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value6)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'tag' and relationId = t.tagid)",
						"improve", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.total)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'tag' and relationId = t.tagid)",
						"total", months);
			} else {

				QueryUtils.addColumn(qb,
						"(select avg(t1.value1) from DataResult t1 where t1.relationId = t.tagid and t1.relationType = 'tag' and t1.delFlag = '0')",
						"study");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value2) from DataResult t1 where t1.relationId = t.tagid and t1.relationType = 'tag' and t1.delFlag = '0')",
						"read");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value3) from DataResult t1 where t1.relationId = t.tagid and t1.relationType = 'tag' and t1.delFlag = '0')",
						"culture");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value4) from DataResult t1 where t1.relationId = t.tagid and t1.relationType = 'tag' and t1.delFlag = '0')",
						"attendance");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value5) from DataResult t1 where t1.relationId = t.tagid and t1.relationType = 'tag' and t1.delFlag = '0')",
						"hse");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value6) from DataResult t1 where t1.relationId = t.tagid and t1.relationType = 'tag' and t1.delFlag = '0')",
						"improve");
				QueryUtils.addColumn(qb,
						"(select avg(t1.total) from DataResult t1 where t1.relationId = t.tagid and t1.relationType = 'tag' and t1.delFlag = '0')",
						"total");
				QueryUtils.addColumn(qb,
						"(select count(t1.id) from OrgRelation t1 where t1.relationType ='tag' and t1.relationId = t.tagid)",
						"personNub");
			}
		}
		if (StringUtils.isNotBlank(inputSearch)) {
			QueryUtils.addWhere(qb, " and  t.tagname like {0}", "%" + inputSearch + "%");
		}
		if (Utilities.isValidId(tagid)) {
			QueryUtils.addWhere(qb, " and  t.tagid = {0}", tagid);
		}

		QueryUtils.addJoin(qb, "left join IgnoreGroups u on u.tagid = t.tagid");
		return orgService.groupDataGrid(qb);
	}

	@GetMapping(value = "deptDataGrid")
	public Object deptDataGrid(UserInfo userInfo, Integer start, Integer length, String deptType, String avg,
			String inputSearch, String startMonth, Integer monthNub, Integer deptId) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.name");
		QueryUtils.addColumn(qb,
				"(select count(t1.id) from OrgRelation t1 where t1.relationId = t.id and t1.relationType = 'dept')",
				"number");
		QueryUtils.addColumn(qb, "t.parentid", "parentid");
		QueryUtils.addColumn(qb, "u.deptType", "deptType");
		QueryUtils.addWhere(qb, "and not exists(select t1.id from OrgDepartment t1 where t1.parentid = t.id)");
		if (StringUtils.equals(avg, "0")) {

		} else {
			if (StringUtils.isNotBlank(startMonth)) {
				List<String> months = Utilities.setMonthList(startMonth, monthNub);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value1)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'dept' and relationId = t.id)",
						"study", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value2)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'dept' and relationId = t.id)",
						"read", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value3)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'dept' and relationId = t.id)",
						"culture", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value4)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'dept' and relationId = t.id)",
						"attendance", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value5)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'dept' and relationId = t.id)",
						"hse", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value6)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'dept' and relationId = t.id)",
						"improve", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.total)/count(t1.id) from DataResult t1 where t1.month in {0} and t1.relationType = 'dept' and relationId = t.id)",
						"total", months);

			} else {
				QueryUtils.addColumn(qb,
						"(select avg(t1.value1) from DataResult t1 where t1.relationId = t.id and t1.relationType = 'dept' and t1.delFlag = '0')",
						"study");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value2) from DataResult t1 where t1.relationId = t.id and t1.relationType = 'dept' and t1.delFlag = '0')",
						"read");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value3) from DataResult t1 where t1.relationId = t.id and t1.relationType = 'dept' and t1.delFlag = '0')",
						"culture");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value4) from DataResult t1 where t1.relationId = t.id and t1.relationType = 'dept' and t1.delFlag = '0')",
						"attendance");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value5) from DataResult t1 where t1.relationId = t.id and t1.relationType = 'dept' and t1.delFlag = '0')",
						"hse");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value6) from DataResult t1 where t1.relationId = t.id and t1.relationType = 'dept' and t1.delFlag = '0')",
						"improve");
				QueryUtils.addColumn(qb,
						"(select avg(t1.total) from DataResult t1 where t1.relationId = t.id and t1.relationType = 'dept' and t1.delFlag = '0')",
						"total");
				QueryUtils.addColumn(qb,
						"(select count(t1.id) from OrgRelation t1 where t1.relationType ='dept' and t1.relationId = t.id)",
						"personNub");

			}
		}
		QueryUtils.addJoin(qb, "left join DeptRelation u on u.deptId = t.id");
		if (StringUtils.isNotBlank(deptType)) {
			if (StringUtils.equals(deptType, DictUtils.DEPT_TYPE_01)) {
				QueryUtils.addWhere(qb, " and (u.id is null or u.deptType = {0})", deptType);
			} else if (StringUtils.equals(deptType, DictUtils.DEPT_TYPE_02)) {
				QueryUtils.addWhere(qb, " and  u.deptType = {0}", deptType);
			}
		}
		if (Utilities.isValidId(deptId)) {
			QueryUtils.addWhere(qb, " and  t.id = {0}", deptId);
		}
		if (StringUtils.isNotBlank(inputSearch)) {
			QueryUtils.addWhere(qb, " and  t.name like {0}", "%" + inputSearch + "%");
		}
		return orgService.deptDataGrid(qb);
	}

	@GetMapping(value = "setDeptType")
	public Object setDeptType(Integer id, String deptType, UserInfo userInfo) {
		return orgService.setDeptType(id, deptType, userInfo);
	}

	@GetMapping(value = "setGroupFlag")
	public Object setGroupFlag(Integer tagid, String ignoreFlag, UserInfo userInfo) {
		return orgService.setGroupFlag(tagid, ignoreFlag, userInfo);
	}
}
