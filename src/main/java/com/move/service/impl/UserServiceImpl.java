package com.move.service.impl;

import com.move.dao.impl.UserDataDao;
import com.move.model.UserData;
import com.move.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Temporal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDataDao userDataDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserData load(Integer id) {
        return userDataDao.getOne(id);
    }
}
