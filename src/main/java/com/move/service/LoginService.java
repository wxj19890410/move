package com.move.service;

import com.move.utils.UserInfo;

public interface LoginService {
    public UserInfo load(String username, String password, String openId) throws Exception;

    public void loadOut(UserInfo userInfo);
}
