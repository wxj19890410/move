package com.move.service.impl;

import com.google.common.collect.Lists;
import com.move.dao.OrgRelationDao;
import com.move.dao.UseDataDao;
import com.move.model.OrgRelation;
import com.move.model.UserData;
import com.move.service.LoginService;
import com.move.utils.*;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class LoginServiveImpl implements LoginService {

	@Autowired
	private UseDataDao useDataDao;

	@Autowired
	private OrgRelationDao orgRelationDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserInfo load(String account, String password, String openId) {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.account = {0}", account);
		QueryUtils.addWhere(qb, "and t.passWord = {0}", password);

		UserData userData = useDataDao.get(qb);
		OrgRelation relation = new OrgRelation();
		UserInfo userInfo = new UserInfo();
		if (null != userData) {

		} else {
			// 账号不存在
		}
		userInfo.setName(userData.getName());
		userInfo.setUserId(userData.getId());
		userInfo.setLoginUuid(UUID.randomUUID().toString());
		userInfo.setLoginDate(new Date());
		userInfo.setOperateDate(new Date());

		synchronized (Globals.USER_INFOS) {
			List<String> keyList = Lists.newArrayList();
			for (String key : Globals.USER_INFOS.keySet()) {
				if (Globals.USER_INFOS.get(key).getUserId().equals(userData.getId())) {
					keyList.add(key);
				}
			}
			if (keyList.size() > 0) {
				for (String key : keyList) {
					Globals.USER_INFOS.remove(key);
				}
			}
			Globals.USER_INFOS.put(userInfo.getLoginUuid(), userInfo);
		}

		return userInfo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void loadOut(UserInfo userInfo) {

	}
}
