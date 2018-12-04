package com.move.action;

import com.google.common.collect.Lists;
import com.move.dao.DataOriginalDao;
import com.move.exception.ValidateException;
import com.move.model.OrgDepartment;
import com.move.model.UserData;
import com.move.service.DataService;
import com.move.service.FileService;
import com.move.service.OrgService;
import com.move.service.UserService;
import com.move.service.WxDataService;
import com.move.utils.DictUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "mobile")
public class MobileAction {

	@Autowired
	private DataService dataService;

	@Autowired
	private OrgService orgService;

	@Autowired
	private WxDataService wxDataService;

	@Autowired
	private FileService fileService;

	@GetMapping(value = "getInfos")
	public Object getInfos(String userid, String month, UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		// 获取本月信息
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.total>t.total and t1.month = t.month and t1.delFlag='0')",
				"rank");
		QueryUtils.addColumn(qb, "u.personNub", "personNub");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value1>t.value1 and t1.month = t.month and t1.delFlag='0')",
				"studyRank");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value2>t.value2 and t1.month = t.month and t1.delFlag='0')",
				"readRank");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value3>t.value3 and t1.month = t.month and t1.delFlag='0')",
				"cultureRank");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value4>t.value4 and t1.month = t.month and t1.delFlag='0')",
				"attendanceRank");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value5>t.value5 and t1.month = t.month and t1.delFlag='0')",
				"hseRank");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataOriginal t1 where t1.value6>t.value6 and t1.month = t.month and t1.delFlag='0')",
				"improveRank");
		QueryUtils.addColumn(qb, "t.total", "total");
		QueryUtils.addColumn(qb, "u.value1", "averStudy");
		QueryUtils.addColumn(qb, "u.value2", "averRead");
		QueryUtils.addColumn(qb, "u.value3", "averCulture");
		QueryUtils.addColumn(qb, "u.value4", "averAttendance");
		QueryUtils.addColumn(qb, "u.value5", "averHse");
		QueryUtils.addColumn(qb, "u.value6", "averImprove");
		QueryUtils.addColumn(qb, "u.value1+u.value2+u.value3+u.value4+u.value5+u.value6", "averTotal");
		QueryUtils.addWhere(qb, "and t.userid = {0}", userid);
		QueryUtils.addJoin(qb,
				"left join DataResult u on u.month = t.month and u.delFlag = '0' and u.relationType='company'");
		QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		Map<String, Object> dataOriginal = dataService.originalMap(qb);
		data.put("data", dataOriginal);
		return data;
	}

	@GetMapping(value = "myHistoryData")
	public Object myHistoryData(String userid, String dataType, UserInfo userInfo) {

		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.month", "month");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.total", "total");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and t.userid = {0}", userid);
		QueryUtils.addOrder(qb, "t.editDate asc");
		return dataService.originalMapList(qb);
	}

	@GetMapping(value = "orgInfo")
	public Object getOrgInfo(UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		// 部门
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addOrder(qb, "t.id ");
		// 组织
		data.put("dept", orgService.findDeptMap(qb));
		qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.tagid", "id");
		QueryUtils.addColumn(qb, "t.tagname", "name");
		/*
		 * QueryUtils.addWhere(qb,
		 * "and exists(select t1.id from OrgRelation t1 where t1.relationId = t.tagid and t1.relationType='tag')"
		 * );
		 */
		QueryUtils.addWhere(qb, " and not exists(from IgnoreGroups u where u.tagid = t.tagid and u.ignoreFlag = '1')");
		QueryUtils.addOrder(qb, "t.tagid");
		data.put("tag", orgService.findGroupMap(qb));
		return data;
	}

	@GetMapping(value = "averData")
	public Object averData(Integer groupId, Integer deptId, String month, UserInfo userInfo) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(0);
		qb.setLength(10);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addColumn(qb, "t.total", "total");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		if (Utilities.isValidId(deptId)) {
			QueryUtils.addColumn(qb,
					"(select count(t1.id)+1 from DataOriginal t1 where t1.total>t.total and t1.month = t.month and t1.delFlag='0' and t1.deptId = {0})",
					"rank", deptId);
			QueryUtils.addWhere(qb,
					"and t.deptId = {0}",
					deptId);
		} else if (Utilities.isValidId(groupId)) {
			QueryUtils.addColumn(qb,
					"(select count(t1.id)+1 from DataOriginal t1 where t1.total>t.total and t1.month = t.month and t1.delFlag='0'  and  exists(from OrgRelation u where u.relationId = {0} and u.relationType = 'tag' and u.userid=t1.userid))",
					"rank", groupId);
			QueryUtils.addWhere(qb,
					"and exists(from OrgRelation t1 where t1.relationId = {0} and t1.relationType = 'tag' and t1.userid=t.userid)",
					groupId);
		} else {
			QueryUtils.addColumn(qb,
					"(select count(t1.id)+1 from DataOriginal t1 where t1.total>t.total and t1.month = t.month and t1.delFlag='0')",
					"rank");
		}
		QueryUtils.addWhere(qb, "and t.month = {0}", month);
		QueryUtils.addOrder(qb, "t.total desc");
		return dataService.DataGrid(qb);
	}

	/***
	 * 手机登录
	 * 
	 * @param userid
	 * @return
	 */
	@GetMapping(value = "loadInfo")
	public Object loadInfo(String codeId, String userid) {
		// 获取 access_token
		return wxDataService.loadInfo(codeId, userid);
	}

	@GetMapping(value = "/dataZip")
	public Object dataZip(UserInfo userInfo) {
		return fileService.dataZip(userInfo);
	}

	@GetMapping(value = "/downLoad")
	public void downLoad(HttpServletResponse response, String path) {
		String rootPath = "C:/huoli/huoliJava";
		String realPath = rootPath + path;
		try {
			// response.reset();
			// response.setHeader("Access-Control-Allow-Origin", "*");
			// response.setHeader("Content-Disposition", "attachment;
			// filename=huolizhishu.zip");
			// response.setContentType("application/x-msdownload");
			/*
			 * response.setHeader("Content-Type", "application/zip");
			 * response.setHeader("Content-Disposition",
			 * "attachment; filename=\"huolizhishu.zip\"");
			 * response.setContentType("application/octet-stream;charset=UTF-8")
			 * ; Streams.copy(new FileInputStream(realPath), new
			 * BufferedOutputStream(response.getOutputStream()), true);
			 */

			// path是指欲下载的文件的路径。
			File file = new File(path);
			// 取得文件名。
			String filename = file.getName();

			InputStream fis = new BufferedInputStream(new FileInputStream(realPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/octet-stream");
			// 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
			response.setHeader("Content-Type", "application/zip");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			toClient.write(buffer);
			toClient.flush();
			toClient.close();

		} catch (Exception e) {
			throw new ValidateException("");
		}
	}
	

	@GetMapping(value = "getDeptRank")
	public Object getDeptRank(String month, UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> data1 = Lists.newArrayList();
		List<Map<String, Object>> data2 = Lists.newArrayList();
		// 生产部门
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "'生产部门'","typeName");
		QueryUtils.addColumn(qb, "'01'","deptType");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataResult t1 where t1.month =t.month and t1.total > t.total and t1.relationType= t.relationType  and not exists(from DeptRelation t2 where t2.deptType='02' and t2.deptId = t1.relationId))",
				"rank");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.total*t.personNub", "total");
		QueryUtils.addColumn(qb, "t.total", "avg");
		QueryUtils.addColumn(qb, "(select t1.name from OrgDepartment t1 where t1.id =t.relationId)", "name");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and t.relationType = 'dept'");
		QueryUtils.addWhere(qb, "and (u.deptType is null or u.deptType = '01')");
		QueryUtils.addJoin(qb, "left join DeptRelation u on u.deptId =t.relationId");
		if (StringUtils.isNotBlank(month)) {
			QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		}
		QueryUtils.addOrder(qb, "t.total desc");
		data1= dataService.resultMapList(qb);
		
		qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "'非生产部门'","typeName");
		QueryUtils.addColumn(qb, "'01'","deptType");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataResult t1 where t1.month =t.month and t1.total > t.total and t1.relationType= t.relationType and exists(from DeptRelation t2 where t2.deptType='02' and t2.deptId = t1.relationId))",
				"rank");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.total*t.personNub", "total");
		QueryUtils.addColumn(qb, "t.total", "avg");
		QueryUtils.addColumn(qb, "(select t1.name from OrgDepartment t1 where t1.id =t.relationId)", "name");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and t.relationType = 'dept'");
		QueryUtils.addWhere(qb, "and u.deptType = '02'");
		QueryUtils.addJoin(qb, "left join DeptRelation u on u.deptId =t.relationId");
		if (StringUtils.isNotBlank(month)) {
			QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		}
		QueryUtils.addOrder(qb, "t.total desc");
		data2= dataService.resultMapList(qb);
		
		data.put("data1", data1);
		data.put("data2", data2);
		return data;
		
	}

	@GetMapping(value = "orgRank")
	public Object orgRank(String month, UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> data1 = Lists.newArrayList();
		List<Map<String, Object>> data2 = Lists.newArrayList();
		// 生产部门
		
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "'横班指数'","typeName");
		QueryUtils.addColumn(qb, "'01'","tagType");
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataResult t1 where t1.month =t.month and t1.total > t.total and t1.relationType= t.relationType and not exists(from GroupRelation t2 where t2.tagType='02' and t2.tagId = t1.relationId)  and not exists(from IgnoreGroups t2 where t2.tagid = t1.relationId and t2.ignoreFlag = '1'))",
				"rank");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.total*t.personNub", "total");
		QueryUtils.addColumn(qb, "t.total", "avg");
		QueryUtils.addColumn(qb, "(select t1.tagname from OrgGroup t1 where t1.tagid =t.relationId)", "name");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and t.relationType = 'tag'");
		QueryUtils.addWhere(qb, "and (u.id is null or u.tagType = '01')");
		QueryUtils.addWhere(qb,
				" and not exists(from IgnoreGroups t1 where t1.tagid = t.relationId and t1.ignoreFlag = '1')");
		QueryUtils.addJoin(qb, "left join GroupRelation u on u.tagId =t.relationId");
		if (StringUtils.isNotBlank(month)) {
			QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		}
		QueryUtils.addOrder(qb, "t.total desc");
		data1 = dataService.resultMapList(qb);

		// 班组
		qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "'设备部指数'","typeName");
		QueryUtils.addColumn(qb, "'02'","tagType");
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb,
				"(select count(t1.id)+1 from DataResult t1 where t1.month =t.month and t1.total > t.total and t1.relationType= t.relationType and exists(from GroupRelation t2 where t2.tagType='02' and t2.tagId = t1.relationId) and not exists(from IgnoreGroups t2 where t2.tagid = t1.relationId and t2.ignoreFlag = '1'))",
				"rank");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.total*t.personNub", "total");
		QueryUtils.addColumn(qb, "t.total", "avg");
		QueryUtils.addColumn(qb, "(select t1.tagname from OrgGroup t1 where t1.tagid =t.relationId)", "name");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and t.relationType = 'tag'");
		if (StringUtils.isNotBlank(month)) {
			QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		}
		QueryUtils.addWhere(qb,
				" and not exists(from IgnoreGroups t1 where t1.tagid = t.relationId and t1.ignoreFlag = '1')");
		QueryUtils.addWhere(qb, "and u.tagType = '02'");
		QueryUtils.addJoin(qb, "left join GroupRelation u on u.tagId =t.relationId");
		QueryUtils.addOrder(qb, "t.total desc");
		data2 = dataService.resultMapList(qb);
		data.put("data1", data1);
		data.put("data2", data2);
		return data;

	}
}
