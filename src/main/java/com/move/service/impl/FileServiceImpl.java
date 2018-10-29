package com.move.service.impl;

import com.move.dao.impl.DataOriginalDao;
import com.move.dao.impl.SysFileDao;
import com.move.model.DataOriginal;
import com.move.model.SysFile;
import com.move.service.FileService;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private SysFileDao sysFileDao;

    @Autowired
    private DataOriginalDao originalDataDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SysFile setFileData(String name,String ext,String path, UserInfo userInfo) {
        SysFile sysFile = new SysFile();
        sysFile.setName(name);
        sysFile.setExt(ext);
        sysFile.setOpenId(userInfo.getOpenID());
        sysFile.setPath(path);
        Utilities.setUserInfo(sysFile,userInfo);
        sysFileDao.save(sysFile);
        try {
            readExcel(path);
        }catch (Exception e) {
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void readExcel(String path) throws Exception  {
        InputStream is = new FileInputStream(new File(ResourceUtils.getURL("classpath:").getPath()+path));
        Workbook hssfWorkbook = null;
        if (path.endsWith("xlsx")) {
            hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
        } else if (path.endsWith("xls")) {
            hssfWorkbook = new HSSFWorkbook(is);//Excel 2003

        }
        // HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        // XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
        DataOriginal originalData = null;
        List<DataOriginal> list = new ArrayList<DataOriginal>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            //HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                //HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    //
                    //HSSFCell name = hssfRow.getCell(0);
                    //HSSFCell pwd = hssfRow.getCell(1);
                    Cell name = hssfRow.getCell(0);
                    Cell pwd = hssfRow.getCell(1);
                    System.out.println("字段" + hssfRow.getRowNum());
                    if(hssfRow.getRowNum()>1){
                        originalData = new DataOriginal();
                        originalData.setValue1(Integer.parseInt(hssfRow.getCell(1).toString()));
                        originalData.setValue2(Integer.parseInt(hssfRow.getCell(2).toString()));
                        originalData.setValue3(Integer.parseInt(hssfRow.getCell(3).toString()));
                        originalData.setValue4(Integer.parseInt(hssfRow.getCell(4).toString()));
                        originalData.setValue5(Integer.parseInt(hssfRow.getCell(5).toString()));
                        originalData.setValue6(Integer.parseInt(hssfRow.getCell(6).toString()));
                        list.add(originalData);
                    }
                }
            }
            originalDataDao.saveAll(list);
        }
    }
}
