package com.move.action;

import com.move.model.UserData;
import com.move.service.LoginService;
import com.move.service.OrgService;
import com.move.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="org")
public class OrgAction {

    @Autowired
    private OrgService orgService;

    @PostMapping(value = "saveGroup")
    public Object saveGroup(Integer id,String name,UserInfo userInfo){
        return orgService.saveGroup(id,name,userInfo);
    }

    @PostMapping(value = "deleteGroup")
    public Object deleteGroup(Integer id,UserInfo userInfo){
        orgService.deleteGroup(id,userInfo);
        return "success";
    }

    @GetMapping(value = "findGroupAll")
    public Object findGroupAll(UserInfo userInfo){
        return orgService.findGroupAll(userInfo);
    }


    @PostMapping(value = "createDept")
    public Object createDept(Integer parentId, UserInfo userInfo){
        return orgService.createDept(parentId,userInfo);
    }
    @PostMapping(value = "updaDept")
    public Object updaDept(Integer id, String name,UserInfo userInfo){
        return orgService.updaDept(id,name,userInfo);
    }

    @PostMapping(value = "deleteDept")
    public Object deleteDept(Integer id,UserInfo userInfo){
         orgService.deleteDept(id,userInfo);
        return id;
    }

    @GetMapping(value = "findDeptAll")
    public Object findDeptAll(UserInfo userInfo){
        return orgService.findDeptAll(userInfo);
    }
}
