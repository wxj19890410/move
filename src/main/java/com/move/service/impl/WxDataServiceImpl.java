package com.move.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.move.dao.IgnoreGroupsDao;
import com.move.dao.MsgHistoryDao;
import com.move.dao.OrgDepartmentDao;
import com.move.dao.OrgGroupDao;
import com.move.dao.OrgRelationDao;
import com.move.dao.UseDataDao;
import com.move.model.IgnoreGroups;
import com.move.model.MsgHistory;
import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
import com.move.model.OrgRelation;
import com.move.model.UserData;
import com.move.model.WeChatData;
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
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.util.HSSFColor.GOLD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
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

	@Autowired
	private MsgHistoryDao msgHistoryDao;

	@Autowired
	private IgnoreGroupsDao ignoreGroupsDao;

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

		if (null != OrgDepartments && OrgDepartments.size() >= 0) {
			// 保存数据
			// 删除之前数据
			QueryBuilder qb = new QueryBuilder();
			QueryUtils.addWhere(qb, " and 1=1");
			orgDepartmentDao.delete(qb);
			for (OrgDepartment orgDepartment : OrgDepartments) {
				orgDepartment.setCreateDate(now);
			}
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
				if (null != lists && lists.size() >= 0) {
					OrgRelation orgRelation = new OrgRelation();
					for (UserData userData : lists) {
						userData.setDeptId(orgDepartment.getId());

						orgRelation = new OrgRelation();
						orgRelation.setRelationId(orgDepartment.getId());
						orgRelation.setRelationType("dept");
						orgRelation.setUserid(userData.getUserid());
						orgRelations.add(orgRelation);
					}
				}
			}

			if (null != orgRelations && orgRelations.size() >= 0) {
				// 删除之前关系
				qb = new QueryBuilder();
				QueryUtils.addWhere(qb, " and relationType='dept'");
				orgRelationDao.delete(qb);
				// 创建关系
				orgRelationDao.batchSave(orgRelations);
			}
			if (null != userDatas && userDatas.size() >= 0) {
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

			for (OrgGroup orgGroup : orgGroups) {
				orgGroup.setCreateDate(now);
			}
			orgGroupDao.batchSave(orgGroups);
			// 保存关系
			List<OrgRelation> orgRelations = Lists.newArrayList();

			qb = new QueryBuilder();
			QueryUtils.addSetColumn(qb, "t.tagNames", "");
			QueryUtils.addWhere(qb, " and 1=1");
			useDataDao.update(qb);

			List<Integer> ignoerIds = Lists.newArrayList();
			qb = new QueryBuilder();
			QueryUtils.addWhere(qb, " and t.ignoreFlag='1'");
			List<IgnoreGroups> ignoreGroups = ignoreGroupsDao.find(qb);
			if (null != ignoreGroups && ignoreGroups.size() > 0) {
				for (IgnoreGroups ignoreGroup : ignoreGroups) {
					ignoerIds.add(ignoreGroup.getTagid());
				}
			}

			int t = 0;
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
				List<String> userids = Lists.newArrayList();

				List<UserData> lists = Lists.newArrayList();
				lists = wxUtilModel.getUserlist();
				if (null != lists && lists.size() > 0) {
					OrgRelation orgRelation = new OrgRelation();
					for (UserData userData : lists) {
						userids.add(userData.getUserid());
						orgRelation = new OrgRelation();
						orgRelation.setRelationId(orgGroup.getTagid());
						orgRelation.setRelationType("tag");
						orgRelation.setUserid(userData.getUserid());
						orgRelations.add(orgRelation);
					}
				}
				if (!ignoerIds.contains(orgGroup.getTagid())) {
					t++;
					qb = new QueryBuilder();
					if (Utilities.equals(t, 1)) {
						QueryUtils.addSetColumn(qb, "t.tagNames", orgGroup.getTagname());
					} else {
						QueryUtils.addSetColumn1(qb, "t.tagNames=concat(t.tagNames,' ',{0})", orgGroup.getTagname());
					}
					QueryUtils.addWhere(qb, " and t.userid in {0}", userids);
					useDataDao.update(qb);
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
		UserData userData = this.getPersonInfo(userid);
		Map<String, Object> result = new HashMap<>();
		if (null != userData && StringUtils.isNotBlank(userData.getUserid())) {
			result.put("position", userData.getPosition());
			result.put("name", userData.getName());
			result.put("mobile", userData.getMobile());
			result.put("avatar1", userData.getAvatar());
			if (StringUtils.isNotBlank(userData.getAvatar())) {
				result.put("avatar", userData.getAvatar().substring(0, userData.getAvatar().length() - 2) + "/100");
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

	private UserData getPersonInfo(String userid) {
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
		return userData;

	}

	@Override
	public Map<String, Object> loadInfo(String codeId, String userid) {
		// 获取 access_token
		String access_token = Globals.ACCESS_TOKEN;
		Date now = new Date();
		if (null == access_token || Globals.EXPIRES_DATE < now.getTime()) {
			access_token = this.getAccessToken();
		}
		if (StringUtils.isNotBlank(codeId) && StringUtils.isBlank(userid)) {
			// 获取人员userid
			String data = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + access_token + "&code="
					+ codeId + "&agentid=" + Globals.CORP_ID;
			;
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
			userid = wxUtilModel.getUserId();
		}
		// userid = "13906748021";
		// 获取个人信息
		UserData userData = this.getPersonInfo(userid);
		Map<String, Object> result = new HashMap<>();
		result.put("userid", userid);
		result.put("userInfo", userData);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Object sendMsg(String content, UserInfo userInfo, String month) {
		String access_token = Globals.ACCESS_TOKEN;
		Date now = new Date();
		if (null == access_token || Globals.EXPIRES_DATE < now.getTime()) {
			access_token = this.getAccessToken();
		}
		String userids = "";
		QueryBuilder qb = new QueryBuilder();

		QueryUtils.addColumn(qb, "d.value1", "study");
		QueryUtils.addColumn(qb, "d.value2", "read");
		QueryUtils.addColumn(qb, "d.value3", "culture");
		QueryUtils.addColumn(qb, "d.value4", "attendance");
		QueryUtils.addColumn(qb, "d.value5", "hse");
		QueryUtils.addColumn(qb, "d.value6", "improve");
		QueryUtils.addColumn(qb, "d.total", "total");
		QueryUtils.addColumn(qb, "t.userid", "userid");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addJoin(qb, "left join DataOriginal d on d.userid = t.userid ");
		QueryUtils.addWhere(qb, "and d.month = {0}", month);
		QueryUtils.addWhere(qb, "and t.account is null");
		QueryUtils.addWhere(qb,
				"and not exists(select t1.id from IgnoreUsers t1 where t1.ignoreFlag='1' and t1.userid=t.userid)");
		List<Map<String, Object>> userDatas = useDataDao.listMap(qb);
		List<MsgHistory> msgHistorys = Lists.newArrayList();
		String response = null;
		if (null != userDatas && userDatas.size() > 0) {
			MsgHistory msgHistory = new MsgHistory();
			for (Map<String, Object> userData : userDatas) {
				response = null;
				userids = "";
				String msg = "您好";
				if (null != userData.get("userid")) {
					userids = userData.get("userid").toString();
					if (null != userData.get("name")) {
						msg = msg + "," + userData.get("name").toString();
					}
					msg = msg + ",上月活力指数数据已产生,您的活力指数明细:";
					if (null != userData.get("study")) {
						msg=msg+DictUtils.DATA_VALUE1+":"+userData.get("study").toString()+" ";
					}
					if (null != userData.get("read")) {
						msg=msg+DictUtils.DATA_VALUE2+":"+userData.get("read").toString()+" ";
					}
					if (null != userData.get("culture")) {
						msg=msg+DictUtils.DATA_VALUE3+":"+userData.get("culture").toString()+" ";
					}
					if (null != userData.get("attendance")) {
						msg=msg+DictUtils.DATA_VALUE4+":"+userData.get("attendance").toString()+" ";
					}
					if (null != userData.get("hse")) {
						msg=msg+DictUtils.DATA_VALUE5+":"+userData.get("hse").toString()+" ";
					}
					if (null != userData.get("improve")) {
						msg=msg+DictUtils.DATA_VALUE6+":"+userData.get("improve").toString()+" ";
					}
					if (null != userData.get("total")) {
						msg=msg+"总计:"+userData.get("total").toString()+" ";
					}

					msgHistory = new MsgHistory();
					msgHistory.setContent(msg);
					msgHistory.setCreateDate(now);
					msgHistory.setCreateName(userInfo.getName());
					msgHistory.setMonth(month);
					msgHistory.setState("1");
					msgHistory.setUserid(userData.get("userid").toString());
					msgHistorys.add(msgHistory);

					String postData = createPostData(userids, "text", Integer.parseInt(Globals.AGENT_ID), "content",
							msg);

					try {
						response = post("utf-8", "application/json",
								"https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=", postData,
								access_token);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		msgHistoryDao.batchSave(msgHistorys);
		return response;
	}

	/**
	 * 创建POST BODY
	 */
	private String createPostData(String touser, String msgtype, int agent_id, String contentKey, String contentValue) {
		WeChatData weChatData = new WeChatData();
		weChatData.setTouser(touser);
		weChatData.setAgentid(agent_id);
		weChatData.setMsgtype(msgtype);
		Map<Object, Object> content = new HashMap<Object, Object>();
		content.put(contentKey, contentValue
				+ ",更多详情点击下方查看:<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx1575c3f8d572890d&redirect_uri=http://huoli.whchlor-alkali.com&response_type=code&scope=snsapi_base&agentid=1000033\">活力指数</a>");
		weChatData.setText(content);
		return JsonUtils.toJson(weChatData);
	}

	/**
	 * POST请求
	 */
	private String post(String charset, String contentType, String url, String data, String token) throws IOException {
		String CONTENT_TYPE = "Content-Type";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url + token);
		httpPost.setHeader(CONTENT_TYPE, contentType);
		httpPost.setEntity(new StringEntity(data, charset));
		CloseableHttpResponse response = httpclient.execute(httpPost);
		String resp;
		try {
			HttpEntity entity = response.getEntity();
			resp = EntityUtils.toString(entity, charset);
			EntityUtils.consume(entity);
		} finally {
			response.close();
		}
		return resp;
	}

}
