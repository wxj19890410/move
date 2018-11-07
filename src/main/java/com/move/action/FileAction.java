package com.move.action;

import com.move.service.FileService;
import com.move.service.LoginService;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping(value="file")
public class FileAction {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "upload")
    public Object uploadPicture(MultipartFile file, HttpServletRequest request, HttpServletResponse response, UserInfo userInfo) throws IllegalStateException, IOException {
        //获取文件需要上传到的路径
        //System.out.println("进入后台成功");
        String path = null;// 文件路径
        String type = null;// 文件类型
        String fileName = file.getOriginalFilename();// 文件原名称
        try {
            String rootPath = ResourceUtils.getURL("classpath:").getPath();
            // 自定义的文件名称
            String trueFileName = file.getOriginalFilename();

            List<String> names = Utilities.split(trueFileName,".");
            if(names.size()>1){
                type = names.get(names.size()-1);
            }
            // 设置存放文件的路径
            path ="/files/"+ UUID.randomUUID().toString();
            
            File dir = new File(rootPath+"/files");
            if(!dir.exists()){
            	dir.mkdir();
            }
            
            // 转存文件到指定的路径
            file.transferTo(new File(rootPath+path));
            String finalPath = "/files/"+ trueFileName;
            return fileService.setFileData(trueFileName,type,path,userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return "文件上传失败";
        }
    }


}
