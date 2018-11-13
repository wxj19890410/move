package com.move.service;

import com.move.model.IgnoreUsers;
import com.move.model.OrgRelation;
import com.move.model.UserData;
import com.move.utils.Datagrid;
import com.move.utils.QueryBuilder;
import com.move.utils.UserInfo;

import java.util.List;

public interface UserService {

    public List<UserData> findUsers(QueryBuilder qb);
    public OrgRelation setUserRelation(String openId,Integer deptId,Integer groupId, UserInfo userInfo);
	public Datagrid userDataGrid(QueryBuilder qb);
	public IgnoreUsers setIgnoreFlag(String userid, String ignoreFlag, UserInfo userInfo);
}
