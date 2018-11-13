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
			String inputSearch,String month,String avg) {
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
		QueryUtils.addWhere(qb, "and t.account is null");
		QueryUtils.addJoin(qb, "left join IgnoreUsers u on u.userid = t.userid");
		if (StringUtils.isNotBlank(inputSearch)) {
			QueryUtils.addWhere(qb, "and (t.name like {0}", "%" + inputSearch + "%");
			QueryUtils.addWhere(qb, "or t.position like {0})", "%" + inputSearch + "%");
		}
		if (StringUtils.equals(avg, "1")) {
			//组织
			
			//数据
			if (StringUtils.isNotBlank(month)) {
				QueryUtils.addColumn(qb, "d.value1", "study");
				QueryUtils.addColumn(qb, "d.value2", "read");
				QueryUtils.addColumn(qb, "d.value3", "culture");
				QueryUtils.addColumn(qb, "d.value4", "attendance");
				QueryUtils.addColumn(qb, "d.value5", "hse");
				QueryUtils.addColumn(qb, "d.value6", "improve");
				QueryUtils.addColumn(qb, "d.total", "total");
				QueryUtils.addJoin(qb, "left join DataOriginal d on d.userid = t.userid ");
				QueryUtils.addWhere(qb, "and d.month = {0}", month);
			} else {

				QueryUtils.addColumn(qb,
						"(select avg(t1.value1) from DataOriginal t1 where t1.userid = t.userid and t1.delFlag = '0')",
						"study");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value2) from DataOriginal t1 where t1.userid = t.userid  and t1.delFlag = '0')",
						"read");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value3) from DataOriginal t1 where t1.userid = t.userid  and t1.delFlag = '0')",
						"culture");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value4) from DataOriginal t1 where t1.userid = t.userid  and t1.delFlag = '0')",
						"attendance");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value5) from DataOriginal t1 where t1.userid = t.userid and t1.delFlag = '0')",
						"hse");
				QueryUtils.addColumn(qb,
						"(select avg(t1.value6) from DataOriginal t1 where t1.userid = t.userid and t1.delFlag = '0')",
						"improve");
				QueryUtils.addColumn(qb,
						"(select avg(t1.total) from DataOriginal t1 where t1.userid = t.userid  and t1.delFlag = '0')",
						"total");
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
