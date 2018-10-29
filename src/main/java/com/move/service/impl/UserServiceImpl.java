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
    public UserInfo load(String username, String password) {
        UserInfo userInfo = new UserInfo();
        UserData userData= userDataDao.findByName(username);
       if(null!=userData){
           if(StringUtils.equals(userData.getPassWord(),password)){
               //登录成功


           }else{
               //密码错误
           }
       }else{
           //账号不存在
       }
        userInfo.setGroupId(11);
        userInfo.setGroupName("第一组");
        userInfo.setName(userData.getName());
        userInfo.setUserId(userData.getId());
        userInfo.setLoginUuid(UUID.randomUUID().toString());
        userInfo.setLoginDate(new Date());

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
    public List<UserData> findAll() {
        return userDataDao.findAll();
    }
}
