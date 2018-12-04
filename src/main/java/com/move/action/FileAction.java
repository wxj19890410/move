package com.move.action;

import com.move.exception.ValidateException;
import com.move.service.FileService;
import com.move.utils.DictUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;

import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipOutputStream;

@CrossOrigin
@RestController
@RequestMapping(value = "file")
public class FileAction {

	@Autowired
	private FileService fileService;

	@RequestMapping(value = "upload")
	public Object uploadPicture(MultipartFile file, String time, HttpServletRequest request,
			HttpServletResponse response, UserInfo userInfo) throws IllegalStateException, IOException {
		// 获取文件需要上传到的路径
		// System.out.println("进入后台成功");
		String path = null;// 文件路径
		String type = null;// 文件类型
		String fileName = file.getOriginalFilename();// 文件原名称
		try {
			String rootPath = "C:/huoli/huoliJava";
			// 自定义的文件名称
			String trueFileName = file.getOriginalFilename();

			List<String> names = Utilities.split(trueFileName, ".");
			if (names.size() > 1) {
				type = names.get(names.size() - 1);
			}
			// 设置存放文件的路径
			path = "/files/" + UUID.randomUUID().toString();

			File dir = new File(rootPath + "/files");
			if (!dir.exists()) {
				dir.mkdir();
			}

			// 转存文件到指定的路径
			file.transferTo(new File(rootPath + path));
			String finalPath = "/files/" + trueFileName;
			return fileService.setFileData(trueFileName, type, path, time, userInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return "文件上传失败";
		}
	}

	@GetMapping(value = "/getExcel")
	public void getExcel(HttpServletResponse response, UserInfo userInfo) {
		String name = "活力指数人员";
		String rootPath = "C:/huoli/huoliJava";

		// 设置存放文件的路径
		String path = rootPath + "/excles/" + UUID.randomUUID().toString();

		fileService.createExcel(path);
		try {
			response.reset();
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Content-Disposition", "attachment; filename=huolizhishu.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			Streams.copy(new FileInputStream(path), new BufferedOutputStream(response.getOutputStream()), true);
		} catch (Exception e) {
			throw new ValidateException("");
		}
	}

	@GetMapping(value = "/getFileDataGrid")
	public Object getFileDataGrid(Integer start, Integer length, String relationType, UserInfo userInfo,
			String startMonth, Integer monthNub, String inputSearch) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.month", "month");
		QueryUtils.addColumn(qb, "(select t1.name from UserData t1 where t1.id = t.createUser)", "userName");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addColumn(qb, "t.path", "path");
		QueryUtils.addColumn(qb, "t.ext", "ext");
		QueryUtils.addColumn(qb, "t.createDate", "createDate");
		QueryUtils.addColumn(qb, "'已上传'", "status");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		if (StringUtils.isNotBlank(relationType)) {
			QueryUtils.addWhere(qb, "and t.relationType = {0}", relationType);
		}
		if (StringUtils.isNotBlank(inputSearch)) {
			QueryUtils.addWhere(qb, "and t.name like {0}", "%" + inputSearch + "%");
		}
		if (StringUtils.isNotBlank(startMonth)) {
			List<String> months = Utilities.setMonthList(startMonth, monthNub);
			QueryUtils.addWhere(qb, "and t.month in {0}", months);
		}
		QueryUtils.addOrder(qb, "t.createDate desc");
		return fileService.fileDatagrid(qb);
	}

	@GetMapping(value = "/deleteFile")
	public Object deleteFile(Integer fileId, String path) {
		return fileService.fileDatagrid(fileId, path);
	}
}
