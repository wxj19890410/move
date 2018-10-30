package com.move.service.impl;

import com.move.dao.impl.DataOriginalDao;
import com.move.dao.impl.DataResultDao;
import com.move.dao.impl.UserDataDao;
import com.move.model.DataOriginal;
import com.move.model.DataResult;
import com.move.service.DataService;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private DataOriginalDao dataOriginalDao;

    @Autowired
    private DataResultDao dataResultDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DataOriginal> findOriginal(String month, Integer fileId, UserInfo userInfo) {
        List<DataOriginal> lists = new ArrayList<DataOriginal>();
        if(StringUtils.isNotBlank(month)&&Utilities.isValidId(fileId)){
            lists = dataOriginalDao.findAllByMonthAndFileId(month,fileId);
        }else if(Utilities.isValidId(fileId)){
            lists = dataOriginalDao.findAllByFileId(fileId);
        }else if(StringUtils.isNotBlank(month)){
            lists = dataOriginalDao.findAllByMonth(month);
        }else{
            lists = dataOriginalDao.findAll();
        }
        return lists;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DataResult> findResult(String month, Integer fileId, UserInfo userInfo) {
        List<DataResult> lists = new ArrayList<DataResult>();
        if(StringUtils.isNotBlank(month)&&Utilities.isValidId(fileId)){
            lists = dataResultDao.findAllByMonthAndFileId(month,fileId);
        }else if(Utilities.isValidId(fileId)){
            lists = dataResultDao.findAllByFileId(fileId);
        }else if(StringUtils.isNotBlank(month)){
            lists = dataResultDao.findAllByMonth(month);
        }else{
            lists = dataResultDao.findAll();
        }
        return lists;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DataResult> setResultData(String month, Integer fileId, UserInfo userInfo) {
        return null;
    }
}
