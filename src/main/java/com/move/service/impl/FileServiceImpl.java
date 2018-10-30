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
    private DataOriginalDao dataOriginalDao;

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
            readExcel(path,name,sysFile.getId(),sysFile.getMonth(),userInfo);
        }catch (Exception e) {
        }
        return sysFile;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void readExcel(String path,String name,Integer fileId ,String month,UserInfo userInfo) throws Exception  {
        InputStream is = new FileInputStream(new File(ResourceUtils.getURL("classpath:").getPath()+path));
        Workbook hssfWorkbook = null;
        if (name.endsWith("xlsx")) {
            hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
        } else if (name.endsWith("xls")) {
            hssfWorkbook = new HSSFWorkbook(is);//Excel 2003
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
                    if(hssfRow.getRowNum()>0){
                        dataOriginal = new DataOriginal();
                        dataOriginal.setValue1((new Double(hssfRow.getCell(1).toString())).intValue());
                        dataOriginal.setValue2((new Double(hssfRow.getCell(2).toString())).intValue());
                        dataOriginal.setValue3((new Double(hssfRow.getCell(3).toString())).intValue());
                        dataOriginal.setValue4((new Double(hssfRow.getCell(4).toString())).intValue());
                        dataOriginal.setValue5((new Double(hssfRow.getCell(5).toString())).intValue());
                        dataOriginal.setValue6((new Double(hssfRow.getCell(6).toString())).intValue());
                        dataOriginal.setFileId(fileId);
                        dataOriginal.setMonth(month);
                        Utilities.setUserInfo(dataOriginal,userInfo);
                        list.add(dataOriginal);
                    }
                }
            }
            //System.out.print("行数:"+list.size());
            dataOriginalDao.saveAll(list);
        }
    }
}
