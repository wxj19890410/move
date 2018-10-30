package com.move.service;

import com.move.model.DataOriginal;
import com.move.model.DataResult;
import com.move.model.UserData;
import com.move.utils.UserInfo;

import java.util.List;

public interface DataService  {
    public List<DataOriginal> findOriginal(String month, Integer fileId, UserInfo userInfo);
    public List<DataResult> findResult(String month,Integer fileId,UserInfo userInfo);
    public List<DataResult> setResultData(String month,Integer fileId,UserInfo userInfo);
}
