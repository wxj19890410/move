package com.move.action;

import com.move.model.UserData;
import com.move.service.LoginService;
import com.move.service.OrgService;
import com.move.utils.DictUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public Object findDept(UserInfo userInfo,String deptType) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id", "id");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		return orgService.findDept(qb);
	}
	
	
	@GetMapping(value = "findGroupMap")
	public Object findGroupMap(UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.name","name");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		return orgService.findGroupMap(qb);
	}

	@GetMapping(value = "findDeptMap")
	public Object findDeptMap(UserInfo userInfo,String deptType) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.deptType");
		QueryUtils.addColumn(qb, "t.name","name");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhereIfNotNull(qb, "and t.deptType = {0}", deptType);
		return orgService.findDeptMap(qb);
	}
	
	@GetMapping(value = "groupDataGrid")
	public Object groupDataGrid(UserInfo userInfo,Integer start,Integer length) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.tagid");
		QueryUtils.addColumn(qb, "t.tagname","name");
		QueryUtils.addColumn(qb, "(select count(t1.id) from OrgRelation t1 where t1.relationType ='tag' and t1.relationId = t.tagid)","personNub");
		
		return orgService.groupDataGrid(qb);
	}
	
	@GetMapping(value = "deptDataGrid")
	public Object deptDataGrid(UserInfo userInfo,Integer start,Integer length) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.name");
		QueryUtils.addColumn(qb, "t.parentid","parentid");
		QueryUtils.addColumn(qb, "(select count(t1.id) from OrgRelation t1 where t1.relationType ='dept' and t1.relationId = t.id)","personNub");
		return orgService.deptDataGrid(qb);
	}
}
