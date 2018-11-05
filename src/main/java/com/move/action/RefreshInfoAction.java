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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "wxData")
public class RefreshInfoAction {

	@Autowired
	private WxDataService wxDataService;

	private String corpid = "wx1575c3f8d572890d";
	private String corpsecret = "P8mS9dDsgD3YNOH5EB3IC8cbpXaHLdQXHJebb81i_Fo";

	// 更新人员
	@GetMapping(value = "refreshUser")
	public Object refreshUser(UserInfo userInfo) {
		// 获取 access_token
		wxDataService.refreshUser(userInfo);


		return null;
	}
	// 更新人员
	@GetMapping(value = "refreshTag")
	public Object refreshTag(UserInfo userInfo) {
		wxDataService.refreshTag(userInfo);
		// 获取 access_token
		String access_token = Globals.ACCESS_TOKEN;
		if (null == access_token) {
			access_token = wxDataService.getAccessToken();
		} else {

		}


		return null;
	}

	@GetMapping(value = "refreshDept")
	public Object refreshDept(UserInfo userInfo) {
		wxDataService.setDept(userInfo);

		return null;
	}
}
