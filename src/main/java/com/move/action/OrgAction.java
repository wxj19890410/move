package com.move.action;

import com.move.model.UserData;
import com.move.service.LoginService;
import com.move.service.OrgService;
import com.move.utils.DictUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;

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
	public Object findGroupMap(UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.tagid", "tagid");
		QueryUtils.addColumn(qb, "t.tagname", "name");
		return orgService.findGroupMap(qb);
	}

	@GetMapping(value = "findDeptMap")
	public Object findDeptMap(UserInfo userInfo, String deptType) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.deptType");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhereIfNotNull(qb, "and t.deptType = {0}", deptType);
		return orgService.findDeptMap(qb);
	}

	@GetMapping(value = "groupDataGrid")
	public Object groupDataGrid(UserInfo userInfo, Integer start, Integer length, String avg, String month,
			String inputSearch) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.tagid");
		QueryUtils.addColumn(qb, "t.tagname", "name");
		QueryUtils.addColumn(qb,
				"(select count(t1.id) from OrgRelation t1 where t1.relationId = t.tagid and t1.relationType = 'tag')",
				"number");
		if (StringUtils.equals(avg, "0")) {

		} else {
			if (StringUtils.isNotBlank(month)) {
				QueryUtils.addColumn(qb, "d.value1", "study");
				QueryUtils.addColumn(qb, "d.value2", "read");
				QueryUtils.addColumn(qb, "d.value3", "culture");
				QueryUtils.addColumn(qb, "d.value4", "attendance");
				QueryUtils.addColumn(qb, "d.value5", "hse");
				QueryUtils.addColumn(qb, "d.value6", "improve");
				QueryUtils.addColumn(qb, "d.total", "total");
				QueryUtils.addColumn(qb, "d.personNub", "personNub");
				QueryUtils.addJoin(qb, "left join DataResult d on d.relationId = t.tagid and d.relationType = 'tag'");
				QueryUtils.addWhere(qb, "and d.month = {0}", month);
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
		return orgService.groupDataGrid(qb);
	}

	@GetMapping(value = "deptDataGrid")
	public Object deptDataGrid(UserInfo userInfo, Integer start, Integer length, String deptType, String avg,
			String month, String inputSearch) {
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
			if (StringUtils.isNotBlank(month)) {
				QueryUtils.addColumn(qb, "d.value1", "study");
				QueryUtils.addColumn(qb, "d.value2", "read");
				QueryUtils.addColumn(qb, "d.value3", "culture");
				QueryUtils.addColumn(qb, "d.value4", "attendance");
				QueryUtils.addColumn(qb, "d.value5", "hse");
				QueryUtils.addColumn(qb, "d.value6", "improve");
				QueryUtils.addColumn(qb, "d.total", "total");
				QueryUtils.addColumn(qb, "d.personNub", "personNub");
				QueryUtils.addJoin(qb, "left join DataResult d on d.relationId = t.id and d.relationType = 'dept'");
				QueryUtils.addWhere(qb, "and d.month = {0}", month);
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
		if (StringUtils.isNotBlank(inputSearch)) {
			QueryUtils.addWhere(qb, " and  t.name like {0}", "%" + inputSearch + "%");
		}
		return orgService.deptDataGrid(qb);
	}

	@GetMapping(value = "setDeptType")
	public Object setDeptType(Integer id, String deptType, UserInfo userInfo) {
		return orgService.setDeptType(id, deptType, userInfo);
	}
}
