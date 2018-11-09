package com.move.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;

import com.move.dao.DataOriginalDao;
import com.move.dao.SysFileDao;
import com.move.model.DataOriginal;
import com.move.model.SysFile;
import com.move.service.FileService;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;

import org.apache.commons.lang.StringUtils;
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
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private SysFileDao sysFileDao;

	@Autowired
	private DataOriginalDao dataOriginalDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SysFile setFileData(String name, String ext, String path, String month, UserInfo userInfo) {
		// 刪除之前數據
		if (StringUtils.isNotBlank(month)) {
			QueryBuilder qb = new QueryBuilder();
			QueryUtils.addWhere(qb, " and month={0}", month);
			sysFileDao.delete(qb);
		}
		SysFile sysFile = new SysFile();
		sysFile.setName(name);
		sysFile.setExt(ext);
		sysFile.setPath(path);
		sysFile.setMonth(month);
		Utilities.setUserInfo(sysFile, userInfo);
		sysFileDao.save(sysFile);
		try {
			readExcel(path, name, sysFile.getId(), sysFile.getMonth(), userInfo);
		} catch (Exception e) {
		}
		return sysFile;
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
							dataOriginal.setName(hssfRow.getCell(1).toString());
						else {
							dataOriginal.setName("用户名缺失");
						}
						if (null != hssfRow.getCell(2)) {
							BigDecimal aa = new BigDecimal(new Double(hssfRow.getCell(2).toString()));
							dataOriginal.setMobile(aa.toPlainString());
						} else {
							dataOriginal.setMobile("联系方式缺失");
						}
						if (matchesString(hssfRow.getCell(3)))
							dataOriginal.setValue1((new Double(hssfRow.getCell(3).toString())).intValue());
						else {
							dataOriginal.setValue1(0);
						}
						if (matchesString(hssfRow.getCell(4)))
							dataOriginal.setValue2((new Double(hssfRow.getCell(4).toString())).intValue());
						else {
							dataOriginal.setValue2(0);
						}
						if (matchesString(hssfRow.getCell(5)))
							dataOriginal.setValue3((new Double(hssfRow.getCell(5).toString())).intValue());
						else {
							dataOriginal.setValue3(0);
						}
						if (matchesString(hssfRow.getCell(6)))
							dataOriginal.setValue4((new Double(hssfRow.getCell(6).toString())).intValue());
						else {
							dataOriginal.setValue4(0);
						}
						if (matchesString(hssfRow.getCell(7)))
							dataOriginal.setValue5((new Double(hssfRow.getCell(7).toString())).intValue());
						else {
							dataOriginal.setValue5(0);
						}
						if (matchesString(hssfRow.getCell(8)))
							dataOriginal.setValue6((new Double(hssfRow.getCell(8).toString())).intValue());
						else {
							dataOriginal.setValue6(0);
						}
						dataOriginal.setFileId(fileId);
						dataOriginal.setMonth(month);
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
}
