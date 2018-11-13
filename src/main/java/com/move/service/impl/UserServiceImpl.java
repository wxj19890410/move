package com.move.service.impl;

import com.move.dao.IgnoreUsersDao;
import com.move.dao.OrgRelationDao;
import com.move.dao.UseDataDao;
import com.move.model.IgnoreUsers;
import com.move.model.OrgRelation;
import com.move.model.UserData;
import com.move.service.UserService;
import com.move.utils.Datagrid;
import com.move.utils.DictUtils;
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

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UseDataDao useDataDao;

	@Autowired
	private OrgRelationDao orgRelationDao;

	@Autowired
	private IgnoreUsersDao ignoreUsersDao;

	@Override
	@Transactional
	public List<UserData> findUsers(QueryBuilder qb) {
		return useDataDao.find(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public OrgRelation setUserRelation(String openId, Integer deptId, Integer groupId, UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and t.openId = {0}", openId);
		OrgRelation orgRelation = orgRelationDao.get(qb);

		if (null != orgRelation) {

		} else {
		}
		return orgRelation;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Datagrid userDataGrid(QueryBuilder qb) {
		return useDataDao.datagrid(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public IgnoreUsers setIgnoreFlag(String userid, String ignoreFlag, UserInfo userInfo) {
		System.out.println(ignoreFlag);
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.userid = {0}", userid);
		ignoreUsersDao.delete(qb);
		Date now = new Date();
		IgnoreUsers ignoreUsers = new IgnoreUsers();
		ignoreUsers.setCreateDate(now);
		ignoreUsers.setEditDate(now);
		ignoreUsers.setIgnoreFlag(ignoreFlag);
		ignoreUsers.setUserid(userid);
		return ignoreUsersDao.save(ignoreUsers);
	}
}
