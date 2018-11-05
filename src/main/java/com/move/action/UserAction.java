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
	public Object userDataGrid(UserInfo userInfo, Integer start, Integer length, String haveGroup, String haveDept) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.userid", "userid");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addColumn(qb, "t.position", "position");
		QueryUtils.addColumn(qb, "t.mobile", "mobile");
		QueryUtils.addColumn(qb, "t.email", "email");
		QueryUtils.addColumn(qb, "t.avatar", "avatar");
		QueryUtils.addWhere(qb, "and t.account is null");
		return userService.userDataGrid(qb);
	}

	@PostMapping(value = "setUserRelation")
	public Object setUserRelation(String openId, Integer deptId, Integer groupId, UserInfo userInfo) {
		return userService.setUserRelation(openId, deptId, groupId, userInfo);
	}

}
