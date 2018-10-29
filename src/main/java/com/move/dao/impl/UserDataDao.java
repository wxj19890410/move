package com.move.dao.impl;

import com.move.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataDao extends JpaRepository<UserData,Integer> {
    public UserData findByName(String name);
}
