package com.move.action;

import com.move.service.DataService;
import com.move.service.OrgService;
import com.move.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="data")
public class DataAction {


    @Autowired
    private DataService dataService;

    @GetMapping(value = "findOriginal")
    public Object findOriginal(String month,Integer fileId,UserInfo userInfo){
        return dataService.findOriginal(month,fileId,userInfo);
    }

    @GetMapping(value = "findResult")
    public Object findResult(String month,Integer fileId,UserInfo userInfo){
        return dataService.findResult(month,fileId,userInfo);
    }

    @PostMapping(value = "setResultData")
    public Object setResultData(String month,Integer fileId,UserInfo userInfo){
        return dataService.setResultData(month,fileId,userInfo);
    }

}
