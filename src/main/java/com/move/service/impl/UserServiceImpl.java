package com.move.service.impl;

import com.move.dao.UserDao;
import com.move.dao.impl.UserDataDao;
import com.move.model.OrgRelation;
import com.move.model.UserData;
import com.move.service.UserService;
import com.move.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDataDao userDataDao;



    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserData> findAll() {
        return userDataDao.findAll();
    }


    @Override
    public OrgRelation setUserRelation(Integer userId, Integer deptId, Integer groupId, UserInfo userInfo) {
        return null;
    }
}
