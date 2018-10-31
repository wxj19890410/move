package com.move.service;

import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
import com.move.model.UserData;
import com.move.utils.UserInfo;

import java.util.List;

public interface OrgService  {
    public OrgDepartment createDept(Integer parentId, UserInfo userInfo);
    public OrgGroup saveGroup(Integer id,String name,UserInfo userInfo);
    public OrgDepartment updaDept(Integer id,String name,UserInfo userInfo);
    public void deleteGroup(Integer id,UserInfo userInfo);
    public void deleteDept(Integer id,UserInfo userInfo);
    public List<OrgGroup> findGroupAll(UserInfo userInfo);
    public List<OrgDepartment> findDeptAll(UserInfo userInfo);
}
