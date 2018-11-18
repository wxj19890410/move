package com.move.action;

import com.alibaba.druid.support.json.JSONUtils;
import com.move.model.UserData;
import com.move.service.UserService;
import com.move.utils.DictUtils;
import com.move.utils.Properties;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "user")
public class UserAction {
	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserAction.class);

	@GetMapping(value = "findUserList")
	public Object findUserData() {
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id", "id");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		return userService.findUsers(qb);
	}

	@GetMapping(value = "userDataGrid")
	public Object userDataGrid(UserInfo userInfo, Integer start, Integer length, String haveGroup, String haveDept,
			String inputSearch, String month, String avg, String mobile, String username, String userid,
			String startMonth, Integer monthNub, Integer deptId, String deptType) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.userid", "userid");
		QueryUtils.addColumn(qb, "u.ignoreFlag", "ignoreFlag");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addColumn(qb, "t.position", "position");
		QueryUtils.addColumn(qb, "t.mobile", "mobile");
		QueryUtils.addColumn(qb, "t.email", "email");
		QueryUtils.addColumn(qb, "t.avatar", "avatar");
		QueryUtils.addColumn(qb, "t.tagNames", "tagNames");
		QueryUtils.addColumn(qb,
				"(case when exists(from DeptRelation t1 where t1.deptId = t.deptId and t1.deptType ='02') then '非生产部门' else  '生产部门' end )",
				"deptType");

		QueryUtils.addColumn(qb, "(select t1.name from OrgDepartment t1 where t1.id = t.deptId)", "deptName");
		QueryUtils.addWhere(qb, "and t.account is null");
		QueryUtils.addJoin(qb, "left join IgnoreUsers u on u.userid = t.userid");
		if (StringUtils.isNotBlank(mobile)) {
			QueryUtils.addWhere(qb, "and t.mobile like {0}", "%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(username)) {
			QueryUtils.addWhere(qb, "and t.name like {0}", "%" + username + "%");
		}
		if (StringUtils.isNotBlank(userid)) {
			QueryUtils.addWhere(qb, "and t.userid like {0}", "%" + userid + "%");
		}
		if (StringUtils.isNotBlank(deptType)) {
			if (StringUtils.equals(deptType, "01")) {
				QueryUtils.addWhere(qb,
						"and not exists(from DeptRelation t1 where t1.deptId = t.deptId and t1.deptType ='02')");
			} else {
				QueryUtils.addWhere(qb,
						"and exists(from DeptRelation t1 where t1.deptId = t.deptId and t1.deptType ='02')");
			}
		}
		if (Utilities.isValidId(deptId)) {
			QueryUtils.addWhere(qb, "and t.deptId = {0}", deptId);
		}
		if (StringUtils.isNotBlank(inputSearch)) {
			QueryUtils.addWhere(qb, "and (t.name like {0}", "%" + inputSearch + "%");
			QueryUtils.addWhere(qb, "or t.position like {0})", "%" + inputSearch + "%");
		}
		if (StringUtils.equals(avg, "1")) {
			// 非禁止的
			QueryUtils.addWhere(qb, "and (u.ignoreFlag is null or u.ignoreFlag = '0')");
			// 组织

			// 数据
			if (StringUtils.isNotBlank(startMonth)) {
				List<String> months = Utilities.setMonthList(startMonth, monthNub);
				QueryUtils.addColumn(qb,
						"(select  max(t1.createDate) from DataOriginal t1 where t1.month in {0} and t1.userid = t.userid)",
						"createDate", months);

				QueryUtils.addColumn(qb,
						"(select count(t2.id)+1 from UserData t2 where (select sum(t1.total) from DataOriginal t1 where t1.month in {0} and t1.userid = t2.userid) > (select sum(t1.total) from DataOriginal t1 where t1.month in {0} and t1.userid = t.userid) )",
						"rank", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value1*100)/count(t1.id) from DataOriginal t1 where t1.month in {0} and t1.userid = t.userid)",
						"study", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value2*100)/count(t1.id) from DataOriginal t1 where t1.month in {0} and t1.userid = t.userid)",
						"read", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value3*100)/count(t1.id) from DataOriginal t1 where t1.month in {0} and t1.userid = t.userid)",
						"culture", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value4*100)/count(t1.id) from DataOriginal t1 where t1.month in {0} and t1.userid = t.userid)",
						"attendance", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value5*100)/count(t1.id) from DataOriginal t1 where t1.month in {0} and t1.userid = t.userid)",
						"hse", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.value6*100)/count(t1.id) from DataOriginal t1 where t1.month in {0} and t1.userid = t.userid)",
						"improve", months);
				QueryUtils.addColumn(qb,
						"(select sum(t1.total*100)/count(t1.id) from DataOriginal t1 where t1.month in {0} and t1.userid = t.userid)",
						"total", months);
			} else {
				QueryUtils.addColumn(qb,
						"(select count(t1.id)+1 from DataResult t1 where t1.total>d.total and t1.relationType = d.relationType and t1.delFlag='0')",
						"rank");
				QueryUtils.addColumn(qb, "d.value1*100", "study");
				QueryUtils.addColumn(qb, "d.value2*100", "read");
				QueryUtils.addColumn(qb, "d.value3*100", "culture");
				QueryUtils.addColumn(qb, "d.value4*100", "attendance");
				QueryUtils.addColumn(qb, "d.value5*100", "hse");
				QueryUtils.addColumn(qb, "d.value6*100", "improve");
				QueryUtils.addColumn(qb, "d.total*100", "total");
				QueryUtils.addColumn(qb, "d.createDate", "createDate");
				QueryUtils.addJoin(qb, "left join DataResult d on d.userid = t.userid ");
			}
		}
		return userService.userDataGrid(qb);
	}

	@GetMapping(value = "setUserRelation")
	public Object setUserRelation(String openId, Integer deptId, Integer groupId, UserInfo userInfo) {
		return userService.setUserRelation(openId, deptId, groupId, userInfo);
	}

	@GetMapping(value = "setIgnoreFlag")
	public Object setIgnoreFlag(String userid, String ignoreFlag, UserInfo userInfo) {
		return userService.setIgnoreFlag(userid, ignoreFlag, userInfo);
	}

}
