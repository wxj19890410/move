package com.move.action;

import com.move.model.UserData;
import com.move.service.LoginService;
import com.move.service.UserService;
import com.move.utils.UserInfo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "login")
public class LoginAction {
	@Autowired
	private LoginService loginService;

	@GetMapping(value = "loadInfo")
	public Object loadInfo(String account, String password, String openId) throws Exception {
		UserInfo userInfo = loginService.load(account, password, openId);
		return userInfo;
	}

	@GetMapping(value = "loadOut")
	public Object loadInfo(UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		UserData userData = new UserData();
		loginService.loadOut(userInfo);
		data.put("success", true);
		return data;
	}
}
