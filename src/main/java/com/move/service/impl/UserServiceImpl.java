package com.move.service.impl;

import com.move.dao.UseDataDao;
import com.move.model.OrgRelation;
import com.move.model.UserData;
import com.move.service.UserService;
import com.move.utils.QueryBuilder;
import com.move.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UseDataDao useDataDao;



    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserData> findUsers(QueryBuilder qb) {
        return useDataDao.find(qb);
    }


    @Override
    public OrgRelation setUserRelation(Integer userId, Integer deptId, Integer groupId, UserInfo userInfo) {
        return null;
    }
}
