package com.move.service.impl;

import com.move.dao.impl.OrgDepartmentDao;
import com.move.dao.impl.OrgGroupDao;
import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
import com.move.service.OrgService;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrgServiceImpl implements OrgService {

    @Autowired
    private OrgDepartmentDao orgDepartmentDao;

    @Autowired
    private OrgGroupDao orgGroupDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrgGroup createGroup(UserInfo userInfo) {
        OrgGroup group = new OrgGroup();
        group.setName("新建组");
        Utilities.setUserInfo(group,userInfo);
        return orgGroupDao.save(group);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrgDepartment createDept(Integer parentId, UserInfo userInfo) {
        OrgDepartment department = new OrgDepartment();
        department.setName("新建部门");
        Utilities.setUserInfo(department,userInfo);
        if(Utilities.isValidId(parentId)){
            department.setParentId(parentId);
        }
        return orgDepartmentDao.save(department);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrgGroup updaGroup(Integer id, String name, UserInfo userInfo) {
        OrgGroup group = orgGroupDao.getOne(id);
        group.setName(name);
        return orgGroupDao.save(group);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrgDepartment updaDept(Integer id, String name, UserInfo userInfo) {
        OrgDepartment department  = orgDepartmentDao.getOne(id);
        department.setName(name);
        return orgDepartmentDao.save(department);
    }

    @Override
    public void deleteGroup(Integer id, UserInfo userInfo) {
        orgGroupDao.deleteById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteDept(Integer id, UserInfo userInfo) {
        orgDepartmentDao.deleteById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<OrgGroup> findGroupAll(UserInfo userInfo) {
        return orgGroupDao.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<OrgDepartment> findDeptAll(UserInfo userInfo) {
        return orgDepartmentDao.findAll();
    }
}
