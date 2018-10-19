package com.move.service.impl;

import com.move.dao.impl.UserDataDao;
import com.move.model.UserData;
import com.move.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDataDao userDataDao;

    @Override
    public UserData load(Integer id) {
        return userDataDao.getOne(id);
    }
}
