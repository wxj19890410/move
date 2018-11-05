package com.move.service.impl;

import com.move.dao.OrgDepartmentDao;
import com.move.dao.OrgGroupDao;
import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
import com.move.service.OrgService;
import com.move.utils.Datagrid;
import com.move.utils.QueryBuilder;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class OrgServiceImpl implements OrgService {

	@Autowired
	private OrgDepartmentDao orgDepartmentDao;

	@Autowired
	private OrgGroupDao orgGroupDao;

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
		//group.setName(name);
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

}
