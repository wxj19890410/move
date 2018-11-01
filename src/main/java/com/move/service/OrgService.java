package com.move.service;

import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
import com.move.model.UserData;
import com.move.utils.QueryBuilder;
import com.move.utils.UserInfo;

import java.util.List;

public interface OrgService {
	

	public OrgGroup saveGroup(Integer id, String name, UserInfo userInfo);

	
	public OrgDepartment saveDept(Integer id, String name, String deptType, UserInfo userInfo);

	public void deleteGroup(Integer id, UserInfo userInfo);

	public void deleteDept(Integer id, UserInfo userInfo);

	public List<OrgGroup> findGroup(QueryBuilder qb);

	public List<OrgDepartment> findDept(QueryBuilder qb);
}
