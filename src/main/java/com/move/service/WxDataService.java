package com.move.service;

import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
import com.move.model.UserData;
import com.move.utils.Datagrid;
import com.move.utils.QueryBuilder;
import com.move.utils.UserInfo;

import java.util.List;
import java.util.Map;

public interface WxDataService {

	public String getAccessToken();

	public Integer setDept(UserInfo userInfo);

	public Integer refreshTag(UserInfo userInfo);

	public Map<String, Object> getUserInfo(String userid);

	public Map<String, Object>  loadInfo(String codeId, String userid);

	public Object sendMsg(String content, UserInfo userInfo, String month);

}
