package com.move.service.impl;

import com.google.gson.JsonObject;
import com.move.dao.OrgRelationDao;
import com.move.dao.UseDataDao;
import com.move.model.OrgRelation;
import com.move.model.UserData;
import com.move.service.UserService;
import com.move.service.WxDataService;
import com.move.utils.Datagrid;
import com.move.utils.DictUtils;
import com.move.utils.Globals;
import com.move.utils.JsonUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

@Service
public class WxDataServiceImpl implements WxDataService {

	@Autowired
	private UseDataDao useDataDao;

	@Autowired
	private OrgRelationDao orgRelationDao;

	@Override
	public String getAccessToken() {
		String data = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + Globals.CORP_ID + "&corpsecret="
				+ Globals.CORP_SECRET;
		String access_token = "";
		// 获取 access_token
		try {
			URL url = new URL(data);
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.err.println("成功");
				InputStream in = httpConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader bufr = new BufferedReader(isr);
				String str;
				System.out.println(httpConnection.getInputStream());

				while ((str = bufr.readLine()) != null) {
					System.out.println(str);
					JsonObject jo = JsonUtils.fromJson(str).getAsJsonObject();
					access_token = jo.get("access_token").toString();

				}
				bufr.close();
				Globals.ACCESS_TOKEN = access_token;

				Globals.EXPIRES_DATE = new Date().getTime() + 7200 * 1000;
				System.err.println(access_token);
			} else {
				System.err.println("失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return access_token;
	}

	@Override
	public Integer setDept(UserInfo userInfo) {
		// 获取 access_token
		String access_token = Globals.ACCESS_TOKEN;
		Date now = new Date();
		if (null == access_token || Globals.EXPIRES_DATE < now.getTime()) {
			access_token = this.getAccessToken();
		}
		String data = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + access_token;
		System.out.println("access_token:"+access_token);
		try {
			URL url = new URL(data);
			System.err.println("data:"+data);
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.err.println("成功222");
				InputStream in = httpConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader bufr = new BufferedReader(isr);
				String str;
				System.err.println("bufr:"+bufr);
				while ((str = bufr.readLine()) != null) {
					System.out.println(str);
					JsonObject jo = JsonUtils.fromJson(str).getAsJsonObject();
					System.out.println(jo);
				}
				bufr.close();
				System.err.println(str);
			} else {
				System.err.println("失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
