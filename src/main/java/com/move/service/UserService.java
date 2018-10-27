package com.move.service;

import com.move.model.UserData;

import java.util.List;

public interface UserService {
    public UserData load(Integer id);
    public List<UserData> findAll();
}
