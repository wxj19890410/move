package com.move.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.move.dao.OrgDepartmentDao;
import com.move.dao.OrgGroupDao;
import com.move.dao.OrgRelationDao;
import com.move.dao.UseDataDao;
import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
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
import com.move.utils.WxUtilModel;

import org.apache.commons.lang.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxDataServiceImpl implements WxDataService {

	@Autowired
	private UseDataDao useDataDao;

	@Autowired
	private OrgDepartmentDao orgDepartmentDao;

	@Autowired
	private OrgGroupDao orgGroupDao;

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
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("contentType", "UTF-8");
			httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream in = httpConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader bufr = new BufferedReader(isr);
				String str;
				while ((str = bufr.readLine()) != null) {
					JsonObject jo = JsonUtils.fromJson(str).getAsJsonObject();
					WxUtilModel wxUtilModel = JsonUtils.fromJson(jo, WxUtilModel.class);
					access_token = wxUtilModel.getAccess_token();

				}
				bufr.close();
				Globals.ACCESS_TOKEN = access_token;
				Globals.EXPIRES_DATE = new Date().getTime() + 7200 * 1000;
			} else {
				System.err.println("失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return access_token;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer setDept(UserInfo userInfo) {
		// 获取 access_token
		String access_token = Globals.ACCESS_TOKEN;
		Date now = new Date();
		if (null == access_token || Globals.EXPIRES_DATE < now.getTime()) {
			access_token = this.getAccessToken();
		}
		String data = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + access_token;
		WxUtilModel wxUtilModel = new WxUtilModel();
		try {
			URL url = new URL(data);
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("contentType", "UTF-8");
			httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream in = httpConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(in, "UTF-8");
				BufferedReader bufr = new BufferedReader(isr);
				String str;
				while ((str = bufr.readLine()) != null) {
					JsonObject jo = JsonUtils.fromJson(str).getAsJsonObject();
					wxUtilModel = JsonUtils.fromJson(jo, WxUtilModel.class);
					System.out.println(str);
				}
				bufr.close();
			} else {
				System.err.println("失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<OrgDepartment> OrgDepartments = wxUtilModel.getDepartment();

		if (null != OrgDepartments && OrgDepartments.size() > 0) {
			// 保存数据
			// 删除之前数据
			QueryBuilder qb = new QueryBuilder();
			QueryUtils.addWhere(qb, " and 1=1");
			orgDepartmentDao.delete(qb);
			orgDepartmentDao.batchSave(OrgDepartments);
			// 保存关系 更新人员
			List<OrgRelation> orgRelations = Lists.newArrayList();

			List<UserData> userDatas = Lists.newArrayList();

			for (OrgDepartment orgDepartment : OrgDepartments) {
				data = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + access_token + "&department_id="
						+ orgDepartment.getId() + "&fetch_child=0";
				wxUtilModel = new WxUtilModel();
				try {
					URL url = new URL(data);
					URLConnection URLconnection = url.openConnection();
					HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
					httpConnection.setRequestProperty("Content-Type", "application/json");
					httpConnection.setRequestProperty("contentType", "UTF-8");
					httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
					int responseCode = httpConnection.getResponseCode();
					if (responseCode == HttpURLConnection.HTTP_OK) {
						InputStream in = httpConnection.getInputStream();
						InputStreamReader isr = new InputStreamReader(in, "UTF-8");
						BufferedReader bufr = new BufferedReader(isr);
						String str;
						while ((str = bufr.readLine()) != null) {
							JsonObject jo = JsonUtils.fromJson(str).getAsJsonObject();
							wxUtilModel = JsonUtils.fromJson(jo, WxUtilModel.class);
							System.out.println(str);
						}
						bufr.close();
					} else {
						System.err.println("失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				List<UserData> lists = Lists.newArrayList();
				lists = wxUtilModel.getUserlist();
				userDatas.addAll(lists);
				if (null != lists && lists.size() > 0) {
					OrgRelation orgRelation = new OrgRelation();
					for (UserData userData : lists) {
						orgRelation = new OrgRelation();
						orgRelation.setRelationId(orgDepartment.getId());
						orgRelation.setRelationType("dept");
						orgRelation.setUserid(userData.getUserid());
						orgRelations.add(orgRelation);
					}
				}
			}

			if (null != orgRelations && orgRelations.size() > 0) {
				// 删除之前关系
				qb = new QueryBuilder();
				QueryUtils.addWhere(qb, " and relationType='dept'");
				orgRelationDao.delete(qb);
				// 创建关系
				orgRelationDao.batchSave(orgRelations);
			}
			if (null != userDatas && userDatas.size() > 0) {
				// 删除人员
				qb = new QueryBuilder();
				QueryUtils.addWhere(qb, " and account is null");
				useDataDao.delete(qb);
				// 创建关系
				useDataDao.batchSave(userDatas);
			}
		}
		return OrgDepartments.size();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer refreshTag(UserInfo userInfo) {
		// 获取 access_token
		String access_token = Globals.ACCESS_TOKEN;
		Date now = new Date();
		if (null == access_token || Globals.EXPIRES_DATE < now.getTime()) {
			access_token = this.getAccessToken();
		}
		String data = "https://qyapi.weixin.qq.com/cgi-bin/tag/list?access_token=" + access_token;
		WxUtilModel wxUtilModel = new WxUtilModel();
		try {
			URL url = new URL(data);
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("contentType", "UTF-8");
			httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream in = httpConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(in, "UTF-8");
				BufferedReader bufr = new BufferedReader(isr);
				String str;
				while ((str = bufr.readLine()) != null) {
					JsonObject jo = JsonUtils.fromJson(str).getAsJsonObject();
					wxUtilModel = JsonUtils.fromJson(jo, WxUtilModel.class);
				}
				bufr.close();
			} else {
				System.err.println("失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<OrgGroup> orgGroups = wxUtilModel.getTaglist();
		if (null != orgGroups && orgGroups.size() > 0) {
			// 保存数据
			QueryBuilder qb = new QueryBuilder();
			QueryUtils.addWhere(qb, " and 1=1");
			orgGroupDao.delete(qb);
			orgGroupDao.batchSave(orgGroups);
			// 保存关系
			List<OrgRelation> orgRelations = Lists.newArrayList();
			for (OrgGroup orgGroup : orgGroups) {
				data = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token=" + access_token + "&tagid="
						+ orgGroup.getTagid();
				wxUtilModel = new WxUtilModel();
				try {
					URL url = new URL(data);
					URLConnection URLconnection = url.openConnection();
					HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
					httpConnection.setRequestProperty("Content-Type", "application/json");
					httpConnection.setRequestProperty("contentType", "UTF-8");
					httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
					int responseCode = httpConnection.getResponseCode();
					if (responseCode == HttpURLConnection.HTTP_OK) {
						InputStream in = httpConnection.getInputStream();
						InputStreamReader isr = new InputStreamReader(in, "UTF-8");
						BufferedReader bufr = new BufferedReader(isr);
						String str;
						while ((str = bufr.readLine()) != null) {
							JsonObject jo = JsonUtils.fromJson(str).getAsJsonObject();
							wxUtilModel = JsonUtils.fromJson(jo, WxUtilModel.class);
							System.out.println(str);
						}
						bufr.close();
					} else {
						System.err.println("失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				List<UserData> lists = Lists.newArrayList();
				lists = wxUtilModel.getUserlist();
				if (null != lists && lists.size() > 0) {
					OrgRelation orgRelation = new OrgRelation();
					for (UserData userData : lists) {
						orgRelation = new OrgRelation();
						orgRelation.setRelationId(orgGroup.getTagid());
						orgRelation.setRelationType("tag");
						orgRelation.setUserid(userData.getUserid());
						orgRelations.add(orgRelation);
					}
				}
			}

			if (null != orgRelations && orgRelations.size() > 0) {
				// 删除之前关系
				qb = new QueryBuilder();
				QueryUtils.addWhere(qb, " and relationType='tag'");
				orgRelationDao.delete(qb);
				// 创建关系
				orgRelationDao.batchSave(orgRelations);
			}
		}

		return orgGroups.size();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Map<String, Object> getUserInfo(String userid) {
		// 获取 access_token
		String access_token = Globals.ACCESS_TOKEN;
		Date now = new Date();
		if (null == access_token || Globals.EXPIRES_DATE < now.getTime()) {
			access_token = this.getAccessToken();
		}
		String data = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + access_token + "&userid=" + userid;
		UserData userData = new UserData();
		try {
			URL url = new URL(data);
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("contentType", "UTF-8");
			httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream in = httpConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(in, "UTF-8");
				BufferedReader bufr = new BufferedReader(isr);
				String str;
				while ((str = bufr.readLine()) != null) {
					JsonObject jo = JsonUtils.fromJson(str).getAsJsonObject();
					userData = JsonUtils.fromJson(jo, UserData.class);
				}
				bufr.close();
			} else {
				System.err.println("失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> result = new HashMap<>();
		if (null != userData && StringUtils.isNotBlank(userData.getUserid())) {
			result.put("position", userData.getPosition());
			result.put("name", userData.getName());
			result.put("mobile", userData.getMobile());
			result.put("avatar1", userData.getAvatar());
			if(StringUtils.isNotBlank(userData.getAvatar())){
				result.put("avatar", userData.getAvatar().substring(0,userData.getAvatar().length()-2) + "/100");
			}
			
		}
		// 获取 部门
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb,
				"(case when t.relationType = 'tag' then (select t1.tagname from  OrgGroup t1 where t1.tagid = t.relationId) else (select t1.name from  OrgDepartment t1 where t1.id = t.relationId) end)",
				"name");
		QueryUtils.addColumn(qb, "t.relationType", "relationType");
		QueryUtils.addWhere(qb, " and userid={0}", userid);
		List<Map<String, Object>> relations = orgRelationDao.datagrid(qb).getRows();
		String dept = null;
		String tag = null;
		if (null != relations && relations.size() > 0) {
			for (Map<String, Object> relation : relations) {
				if (null != relation.get("relationType")) {
					if (StringUtils.equals(relation.get("relationType").toString(), "tag")) {
						if (StringUtils.isNotBlank(tag)) {
							tag = tag + "," + relation.get("name").toString();
						} else {
							tag = relation.get("name").toString();
						}

					} else {
						if (StringUtils.isNotBlank(dept)) {
							dept = dept + "," + relation.get("name").toString();
						} else {
							dept = relation.get("name").toString();
						}
					}
				}
			}
		}
		result.put("tag", tag);
		result.put("dept", dept);
		return result;
	}

}
