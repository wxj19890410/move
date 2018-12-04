package com.move.service.impl;

import com.move.dao.DeptRelationDao;
import com.move.dao.GroupRelationDao;
import com.move.dao.IgnoreGroupsDao;
import com.move.dao.OrgDepartmentDao;
import com.move.dao.OrgGroupDao;
import com.move.model.DeptRelation;
import com.move.model.GroupRelation;
import com.move.model.IgnoreGroups;
import com.move.model.IgnoreUsers;
import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
import com.move.service.OrgService;
import com.move.utils.Datagrid;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrgServiceImpl implements OrgService {

	@Autowired
	private OrgDepartmentDao orgDepartmentDao;

	@Autowired
	private OrgGroupDao orgGroupDao;

	@Autowired
	private DeptRelationDao deptRelationDao;

	@Autowired
	private IgnoreGroupsDao ignoreGroupsDao;


	@Autowired
	private GroupRelationDao groupRelationDao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public OrgDepartment saveDept(Integer id, String name, String deptType, UserInfo userInfo) {
		OrgDepartment department = new OrgDepartment();
		if (Utilities.isValidId(id)) {
			department = orgDepartmentDao.get(id);
		}
		department.setName(name);
		// Utilities.setUserInfo(department, userInfo);
		if (Utilities.isValidId(id)) {
			orgDepartmentDao.update(department);
		} else {
			orgDepartmentDao.save(department);
		}
		return department;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public OrgGroup saveGroup(Integer id, String name, UserInfo userInfo) {
		OrgGroup group = new OrgGroup();
		if (Utilities.isValidId(id)) {
			group = orgGroupDao.get(id);
		}
		// group.setName(name);
		if (Utilities.isValidId(id)) {
			orgGroupDao.update(group);
		} else {
			orgGroupDao.save(group);
		}
		return group;
	}

	@Override
	public void deleteGroup(Integer id, UserInfo userInfo) {
		orgGroupDao.delete(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteDept(Integer id, UserInfo userInfo) {
		orgDepartmentDao.delete(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<OrgGroup> findGroup(QueryBuilder qb) {
		return orgGroupDao.find(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<OrgDepartment> findDept(QueryBuilder qb) {
		return orgDepartmentDao.find(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map<String, Object>> findGroupMap(QueryBuilder qb) {
		// TODO Auto-generated method stub
		return orgGroupDao.listMap(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map<String, Object>> findDeptMap(QueryBuilder qb) {
		return orgDepartmentDao.listMap(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Datagrid groupDataGrid(QueryBuilder qb) {
		return orgGroupDao.datagrid(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Datagrid deptDataGrid(QueryBuilder qb) {
		return orgDepartmentDao.datagrid(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public DeptRelation setDeptType(Integer id, String deptType, UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.deptId = {0}", id);
		deptRelationDao.delete(qb);
		Date now = new Date();
		DeptRelation deptRelation = new DeptRelation();
		deptRelation.setCreateDate(now);
		deptRelation.setEditDate(now);
		deptRelation.setDeptType(deptType);
		deptRelation.setDeptId(id);
		return deptRelationDao.save(deptRelation);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public IgnoreGroups setGroupFlag(Integer tagid, String ignoreFlag, UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.tagid = {0}", tagid);
		ignoreGroupsDao.delete(qb);
		Date now = new Date();
		IgnoreGroups ignoreGroups = new IgnoreGroups();
		ignoreGroups.setCreateDate(now);
		ignoreGroups.setEditDate(now);
		ignoreGroups.setIgnoreFlag(ignoreFlag);
		ignoreGroups.setTagid(tagid);
		return ignoreGroupsDao.save(ignoreGroups);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Object setGroupType(Integer id, String groupType, UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.tagId = {0}", id);
		groupRelationDao.delete(qb);
		Date now = new Date();
		GroupRelation groupRelation = new GroupRelation();
		groupRelation.setCreateDate(now);
		groupRelation.setEditDate(now);
		groupRelation.setTagType(groupType);
		groupRelation.setTagId(id);
		return groupRelationDao.save(groupRelation);
	}

}
