package com.move.action;


import com.move.model.UserData;
import com.move.service.LoginService;
import com.move.service.UserService;
import com.move.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="login")
public class LoginAction {
    @Autowired
    private LoginService loginService;

    @PostMapping(value = "loadInfo")
    public Object loadInfo(String account, String password){
        UserInfo userInfo=loginService.load(account,password);
        return userInfo;
    }

    @PostMapping(value = "loadOut")
    public Object loadInfo(UserInfo userInfo){
        UserData userData = new UserData();
        loginService.loadOut(userInfo);
        return userData;
    }
}
