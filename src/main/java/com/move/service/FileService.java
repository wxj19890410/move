package com.move.service;

import com.move.model.SysFile;
import com.move.utils.Datagrid;
import com.move.utils.QueryBuilder;
import com.move.utils.UserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	public SysFile setFileData(String name, String ext, String path, String month, UserInfo userInfo);

	public void createExcel(String path);

	public SysFile dataZip(UserInfo userInfo);

	public Datagrid fileDatagrid(QueryBuilder qb);

	public Integer fileDatagrid(Integer fileId, String path);

}
