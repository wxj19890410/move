package com.move.service;

import com.move.model.DeptRelation;
import com.move.model.IgnoreGroups;
import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
import com.move.model.UserData;
import com.move.utils.Datagrid;
import com.move.utils.QueryBuilder;
import com.move.utils.UserInfo;

import java.util.List;
import java.util.Map;

public interface OrgService {
	

	public OrgGroup saveGroup(Integer id, String name, UserInfo userInfo);

	
	public OrgDepartment saveDept(Integer id, String name, String deptType, UserInfo userInfo);

	public void deleteGroup(Integer id, UserInfo userInfo);

	public void deleteDept(Integer id, UserInfo userInfo);

	public List<OrgGroup> findGroup(QueryBuilder qb);

	public List<OrgDepartment> findDept(QueryBuilder qb);


	public List<Map<String, Object>> findGroupMap(QueryBuilder qb);


	public List<Map<String, Object>> findDeptMap(QueryBuilder qb);


	public Datagrid groupDataGrid(QueryBuilder qb);


	public Datagrid deptDataGrid(QueryBuilder qb);


	public DeptRelation setDeptType(Integer id, String deptType, UserInfo userInfo);


	public IgnoreGroups setGroupFlag(Integer tagid, String ignoreFlag, UserInfo userInfo);
}
