package com.move.action;


import com.alibaba.druid.support.json.JSONUtils;
import com.move.model.UserData;
import com.move.service.UserService;

import com.move.utils.Properties;
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
@RequestMapping(value="user")
public class UserAction {
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserAction.class);

    @GetMapping(value = "findUserData")
    public Object findUserData(){
        return userService.findAll();
    }

    @PostMapping(value = "upload")
    public Object uploadPicture(MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
        //获取文件需要上传到的路径
        //System.out.println("进入后台成功");
        String path = null;// 文件路径
        String type = null;// 文件类型
        String fileName = file.getOriginalFilename();// 文件原名称
        System.out.println("上传的文件原名称:"+fileName);
        try {

            System.out.println("路径111:"+request.getServletPath());
            // 项目在容器中实际发布运行的根路径
            String realPath = request.getSession().getServletContext().getRealPath("/");
            // 自定义的文件名称
            String trueFileName = file.getOriginalFilename();

            System.out.println("文件名称111:"+file.getOriginalFilename());
            System.out.println("文件名称:"+file.getContentType());
            // 设置存放图片文件的路径
            path = realPath+ trueFileName;
            System.out.println("存放图片文件的路径:classpath:"+ResourceUtils.getURL("classpath:").getPath());
            // 转存文件到指定的路径
            file.transferTo(new File(ResourceUtils.getURL("classpath:").getPath()+"/files/"+ trueFileName));
            System.out.println("文件成功上传到指定目录下");

            //得到所有数据
             readExcel(ResourceUtils.getURL("classpath:").getPath()+"/files/"+ trueFileName);


            return "文件上传成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "文件上传失败";
        }
    }

    public  static void readExcel(String path) throws Exception {

        InputStream is = new FileInputStream(new File(path));
        Workbook hssfWorkbook = null;
        if (path.endsWith("xlsx")) {
            hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
        } else if (path.endsWith("xls")) {
            hssfWorkbook = new HSSFWorkbook(is);//Excel 2003

        }
        // HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        // XSSFWorkbook hssfWorkbook = new XSSFWorkbook(is);
       /* User student = null;
        List<User> list = new ArrayList<User>();*/
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
                    // student = new User();
                    //HSSFCell name = hssfRow.getCell(0);
                    //HSSFCell pwd = hssfRow.getCell(1);
                    Cell name = hssfRow.getCell(0);
                    Cell pwd = hssfRow.getCell(1);
                    System.out.println("字段" + hssfRow.getRowNum());
                    System.out.println("字段1" + hssfRow.getCell(1));
                    System.out.println("字段2" + hssfRow.getCell(4));
//这里是自己的逻辑
                 /*   student.setUserName(name.toString());
                    student.setPassword(pwd.toString());*/

                    //list.add(student);
                }
            }
        }
    }

}
