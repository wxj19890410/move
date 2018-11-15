package com.move.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

/**
 * zip压缩帮助类
 * 
 * @author Administrator
 * 
 */
public class ZipUtils {

	public static void zip(List<String> srcFiles, String destFile) throws IOException {
		ArchiveOutputStream os = null;
		BufferedInputStream is = null;

		try {

			os = new ZipArchiveOutputStream(new FileOutputStream(destFile));

			for (String fileName : srcFiles) {
				File file = new File(fileName);
				if(file.exists()){
					ArchiveEntry entry = new ZipArchiveEntry(file, file.getName());
					is = new BufferedInputStream(new FileInputStream(file));
					os.putArchiveEntry(entry);
					IOUtils.copy(is, os);
					is.close();
					os.closeArchiveEntry();
				}
			}
			os.flush();
			os.close();

		} finally {
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}
	}

	public static List<String> unzip(String srcFile, String destDir) throws IOException {
		ArchiveInputStream is = null;
		BufferedOutputStream os = null;

		List<String> names = Lists.newArrayList();

		try {
			File directory = new File(destDir);

			if (!directory.exists()) {
				directory.mkdirs();
			}

			is = new ZipArchiveInputStream(new FileInputStream(srcFile));
			ArchiveEntry entry = null;

			while ((entry = is.getNextEntry()) != null) {
				File file = new File(destDir + "/" + entry.getName());
				os = new BufferedOutputStream(new FileOutputStream(file));

				IOUtils.copy(is, os);
				os.close();

				names.add(entry.getName());
			}

			is.close();
		} finally {
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
		}

		return names;
	}
}