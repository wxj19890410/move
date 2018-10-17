package com.move.action;


import com.move.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="user")
public class UserAction {
    @Autowired
    private UserService userService;

    @GetMapping(value = "loadInfo")
    public Object loadInfo(Integer id){


       return userService.load(id);
    }




}
