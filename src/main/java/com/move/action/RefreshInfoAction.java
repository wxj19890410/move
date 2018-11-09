package com.move.action;

import com.google.gson.JsonObject;
import com.move.service.DataService;
import com.move.service.OrgService;
import com.move.service.WxDataService;
import com.move.utils.DictUtils;
import com.move.utils.Globals;
import com.move.utils.JsonUtils;
import com.move.utils.Properties;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "wxData")
public class RefreshInfoAction {

	@Autowired
	private WxDataService wxDataService;

	private String corpid = "wx1575c3f8d572890d";
	private String corpsecret = "P8mS9dDsgD3YNOH5EB3IC8cbpXaHLdQXHJebb81i_Fo";

	// 更新人员
	@GetMapping(value = "getUserInfo")
	public Object refreshUser(String userid,UserInfo userInfo) {
		// 获取 access_token
		return wxDataService.getUserInfo(userid);
	}
	// 更新班组
	@GetMapping(value = "refreshTag")
	public Object refreshTag(UserInfo userInfo) {
		

		return wxDataService.refreshTag(userInfo);
	}
	
	// 更新部
	@GetMapping(value = "refreshDept")
	public Object refreshDept(UserInfo userInfo) {
		return wxDataService.setDept(userInfo);
	}
}
