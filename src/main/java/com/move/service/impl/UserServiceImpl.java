package com.move.service.impl;

import com.google.common.collect.Lists;
import com.move.dao.impl.UserDataDao;
import com.move.model.UserData;
import com.move.service.UserService;
import com.move.utils.Globals;
import com.move.utils.UserInfo;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Temporal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDataDao userDataDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserData> findAll() {
        return userDataDao.findAll();
    }
}
