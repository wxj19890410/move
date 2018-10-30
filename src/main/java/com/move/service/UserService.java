package com.move.service;

import com.move.model.OrgRelation;
import com.move.model.UserData;
import com.move.utils.UserInfo;

import java.util.List;

public interface UserService {

    public List<UserData> findAll();
    public OrgRelation setUserRelation(Integer userId,Integer deptId,Integer groupId, UserInfo userInfo);
}
