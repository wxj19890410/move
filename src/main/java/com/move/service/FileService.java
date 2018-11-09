package com.move.service;

import com.move.model.SysFile;
import com.move.utils.UserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public SysFile setFileData(String name,String ext,String path, String month, UserInfo userInfo);
}
