package com.move.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;

import com.google.common.collect.Lists;
import com.move.dao.DataOriginalDao;
import com.move.dao.SysFileDao;
import com.move.dao.UseDataDao;
import com.move.dao.impl.UserDataDaoImpl;
import com.move.model.DataOriginal;
import com.move.model.SysFile;
import com.move.model.UserData;
import com.move.service.DataService;
import com.move.service.FileService;
import com.move.utils.DataBaseUtil;
import com.move.utils.Datagrid;
import com.move.utils.DictUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;
import com.move.utils.ZipUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private SysFileDao sysFileDao;

	@Autowired
	private DataOriginalDao dataOriginalDao;

	@Autowired
	private UseDataDao useDataDao;

	@Autowired
	private DataService dataService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SysFile setFileData(String name, String ext, String path, String month, UserInfo userInfo) {
		// 刪除之前數據
		if (StringUtils.isNotBlank(month)) {
			QueryBuilder qb = new QueryBuilder();
			QueryUtils.addWhere(qb, " and month={0}", month);
			sysFileDao.delete(qb);
		}
		SysFile sysFile = this.saveSysFile(name, ext, path, month, "file", userInfo);
		try {
			readExcel(path, name, sysFile.getId(), sysFile.getMonth(), userInfo);
		} catch (Exception e) {
		}
		dataService.setAverageData(month, null, userInfo);
		return sysFile;
	}

	private SysFile saveSysFile(String name, String ext, String path, String month, String relationType,
			UserInfo userInfo) {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM");
		SysFile sysFile = new SysFile();
		sysFile.setName(name);
		sysFile.setExt(ext);
		sysFile.setPath(path);
		sysFile.setMonth(month);
		sysFile.setRelationType(relationType);
		if (StringUtils.isNotBlank(month)) {
			try {
				sysFile.setEditDate(format1.parse(month));
			} catch (ParseException e) {

			}
		}

		Utilities.setUserInfo(sysFile, userInfo);
		return sysFileDao.save(sysFile);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void readExcel(String path, String name, Integer fileId, String month, UserInfo userInfo) throws Exception {
		String rootPath = "C:/huoli/huoliJava";
		InputStream is = new FileInputStream(new File(rootPath + path));
		Workbook hssfWorkbook = null;
		if (name.endsWith("xlsx")) {
			hssfWorkbook = new XSSFWorkbook(is);// Excel 2007
		} else if (name.endsWith("xls")) {
			hssfWorkbook = new HSSFWorkbook(is);// Excel 2003
		}
		DataOriginal dataOriginal = null;
		List<DataOriginal> list = new ArrayList<DataOriginal>();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM");
		Date monthDate = null;
		if (StringUtils.isNotBlank(month)) {
			monthDate = format1.parse(month);
		}
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					if (hssfRow.getRowNum() > 0) {
						dataOriginal = new DataOriginal();
						
						if (null != hssfRow.getCell(1))
							dataOriginal.setUserid(hssfRow.getCell(1).toString());
						else {
							dataOriginal.setName("用户缺失");
						}

						if (null != hssfRow.getCell(2))
							dataOriginal.setName(hssfRow.getCell(2).toString());
						else {
							dataOriginal.setName("用户名缺失");
						}
						if (null != hssfRow.getCell(3)) {
							BigDecimal aa = new BigDecimal(new Double(hssfRow.getCell(3).toString()));
							dataOriginal.setMobile(aa.toPlainString());
						} else {
							dataOriginal.setMobile("联系方式缺失");
						}
						if (matchesString(hssfRow.getCell(7)))
							dataOriginal.setValue1((new Double(hssfRow.getCell(7).toString())).intValue());
						else {
							dataOriginal.setValue1(0);
						}
						if (matchesString(hssfRow.getCell(8)))
							dataOriginal.setValue2((new Double(hssfRow.getCell(8).toString())).intValue());
						else {
							dataOriginal.setValue2(0);
						}
						if (matchesString(hssfRow.getCell(9)))
							dataOriginal.setValue3((new Double(hssfRow.getCell(9).toString())).intValue());
						else {
							dataOriginal.setValue3(0);
						}
						if (matchesString(hssfRow.getCell(10)))
							dataOriginal.setValue4((new Double(hssfRow.getCell(10).toString())).intValue());
						else {
							dataOriginal.setValue4(0);
						}
						if (matchesString(hssfRow.getCell(11)))
							dataOriginal.setValue5((new Double(hssfRow.getCell(11).toString())).intValue());
						else {
							dataOriginal.setValue5(0);
						}
						if (matchesString(hssfRow.getCell(12)))
							dataOriginal.setValue6((new Double(hssfRow.getCell(12).toString())).intValue());
						else {
							dataOriginal.setValue6(0);
						}
						dataOriginal.setFileId(fileId);
						dataOriginal.setMonth(month);
						dataOriginal.setEditDate(monthDate);
						Utilities.setUserInfo(dataOriginal, userInfo);
						list.add(dataOriginal);

					}
				}
			}
			// 刪除之前數據
			QueryBuilder qb = new QueryBuilder();
			QueryUtils.addWhere(qb, " and month = {0}", month);
			dataOriginalDao.delete(qb);

			dataOriginalDao.batchSave(list);
			// System.out.print("行数:"+list.size());
		}
	}

	private Boolean matchesString(Object data) {
		String reg = "^[0-9]+(.[0-9]+)?$";
		return data != null && !"".equals(data.toString().trim()) && data.toString().matches(reg);
	}

	@Override
	public void createExcel(String path) {
		// 定义表头
		String[] title = { "序号", "系统ID", "姓名", "手机号", "主部门", "子部门", "班组", "学习指数", "读书指数", "企业文化", "出勤指数", "HSE", "精益指数" };
		// 创建excel工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建工作表sheet
		HSSFSheet sheet = workbook.createSheet();
		// 创建第一行
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = null;
		// 插入第一行数据的表头
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
		}
		// 获取人员数据
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.mobile");
		QueryUtils.addColumn(qb, "t.userid");
		QueryUtils.addColumn(qb, "t.name");
		QueryUtils.addColumn(qb, "t.tagNames", "tagNames");
		QueryUtils.addColumn(qb,
				"(case when exists(from DeptRelation t1 where t1.deptId = t.deptId and t1.deptType ='02') then '非生产部门' else  '生产部门' end )",
				"deptType");
		QueryUtils.addColumn(qb, "(select t1.name from OrgDepartment t1 where t1.id = t.deptId)", "deptName");
		QueryUtils.addWhere(qb, "and t.account is null");
		List<Map<String, Object>> users = useDataDao.listMap(qb);
		if (null != users && users.size() > 0) {
			// 写入数据
			for (int i = 0; i < users.size(); i++) {
				Map<String, Object> user = users.get(i);
				HSSFRow nrow = sheet.createRow(i + 1);
				HSSFCell ncell = nrow.createCell(0);
				int index = i + 1;
				ncell.setCellValue("" + index);
				if (null != user.get("userid")) {
					ncell = nrow.createCell(1);
					ncell.setCellValue(user.get("userid").toString());
				}
				if (null != user.get("name")) {
					ncell = nrow.createCell(2);
					ncell.setCellValue(user.get("name").toString());
				}
				if (null != user.get("mobile")) {
					ncell = nrow.createCell(3);
					ncell.setCellValue(user.get("mobile").toString());
				}
				if (null != user.get("deptType")) {
					ncell = nrow.createCell(4);
					ncell.setCellValue(user.get("deptType").toString());
				}
				if (null != user.get("deptName")) {
					ncell = nrow.createCell(5);
					ncell.setCellValue(user.get("deptName").toString());
				}
				if (null != user.get("tagNames")) {
					ncell = nrow.createCell(6);
					ncell.setCellValue(user.get("tagNames").toString());
				}
			}

		}
		/*
		 * // 写入数据 for (int i = 1; i <= 10; i++) { HSSFRow nrow =
		 * sheet.createRow(i); HSSFCell ncell = nrow.createCell(0);
		 * ncell.setCellValue("" + i); ncell = nrow.createCell(1);
		 * ncell.setCellValue("user" + i); ncell = nrow.createCell(2);
		 * ncell.setCellValue("24"); }
		 */
		// 创建excel文件
		File file = new File(path);
		try {
			file.createNewFile();
			// 将excel写入
			FileOutputStream stream = FileUtils.openOutputStream(file);
			workbook.write(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SysFile dataZip(UserInfo userInfo) {
		// String dbName = "huoli";
		String dbName = "WanHuaHuoliDb";
		// 查询所有附件
		String rootPath = "C:/huoli/huoliJava";

		String zipPath = "/files/" + UUID.randomUUID().toString() + ".zip";

		List<String> fileNames = Lists.newArrayList();

		String name = UUID.randomUUID().toString(); // 数据库名
		String bakPath = "/files/" + name + ".bak";// name文件名
		try {
			String realPath = rootPath + bakPath;// name文件名
			String bakSQL = "backup database " + dbName + " to disk=? with init";// SQL语句
			PreparedStatement bak = DataBaseUtil.getConnection().prepareStatement(bakSQL);
			bak.setString(1, realPath);// path必须是绝对路径
			bak.execute(); // 备份数据库
			bak.close();
			fileNames.add(realPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.delFlag = '0'");
		QueryUtils.addWhere(qb, "and t.relationType = 'file'");
		List<SysFile> sysFiles = sysFileDao.find(qb);
		if (null != sysFiles && sysFiles.size() > 0) {
			for (SysFile sysFile : sysFiles) {
				fileNames.add(rootPath + sysFile.getPath());
			}
		}
		try {
			ZipUtils.zip(fileNames, Utilities.getFilePath(rootPath + zipPath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Date now = new Date();
		String zipName = "备份数据" + Utilities.formatDate(now, "YYYY-MM");
		return this.saveSysFile(zipName, "zip", zipPath, null, "zip", userInfo);
	}

	@Override
	public Datagrid fileDatagrid(QueryBuilder qb) {
		return sysFileDao.datagrid(qb);
	}

	@Override
	public Integer fileDatagrid(Integer fileId, String path) {
		String rootPath = "C:/huoli/huoliJava";

		File file = new File(rootPath + path);
		if (!file.exists()) {
			System.out.println("删除文件失败:" + file.getName() + "不存在！");
		} else {
			if (file.isFile()) {
				file.delete();
			}
		}
		// TODO Auto-generated method stub
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addSetColumn(qb, "t.delFlag", DictUtils.YES);
		QueryUtils.addWhere(qb, "and t.id = {0}", fileId);
		return sysFileDao.update(qb);
	}

}
