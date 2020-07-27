package com.fpg.ec.utility.file;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.ice.tar.TarArchive;
import com.ice.tar.TarProgressDisplay;

/**
 * 壓縮檔處理程式
 * @author N000101814
 * 1. Java Zip
 * 		API: http://download.oracle.com/javase/1.5.0/docs/api/java/util/zip/package-summary.html
 * 2. Java Tar
 * 		Website: http://www.trustice.com/java/tar/index.shtml  
 * 		API: http://www.gjt.org/javadoc/com/ice/tar/package-summary.html
 */
public class CompressFileUtil {
	/**
	 * 解Tar檔(unix-like system compressed file)
	 */
	public static void decompressTarFile(String sourFileName, String targetFolderName) throws Exception {
		File srcFile = new File(sourFileName);
		File targetFolder = new File(targetFolderName);
		decompressTarFile(srcFile, targetFolder);
	}

	/**
	 * 解Tar檔(unix-like system compressed file)
	 */
	public static void decompressTarFile(File srcFile, File targetFolder) throws Exception {
		TarArchive tarArchive = new TarArchive(new FileInputStream(srcFile));
		tarArchive.setVerbose(true);
		tarArchive.setTarProgressDisplay(new TarProgressDisplay() {
			public void showTarProgressMessage(String msg) {
				System.out.println("TarProgressMessage:" + msg);
			}
		});
		tarArchive.extractContents(targetFolder);
		tarArchive.closeArchive();
	}

	/**
	 * 取得壓縮檔串流(傳入檔案路徑ArrayList)
	 */
	public static ByteArrayOutputStream getZipFileByteArrayOutputStream(ArrayList filePathList) throws Exception {
		File[] fileList = new File[filePathList.size()];
		for (int i = 0; i < filePathList.size(); i++) {
			String strFilePath = (String) filePathList.get(i);
			fileList[i] = new File(strFilePath);
		}
		return getZipFileByteArrayOutputStream(fileList);
	}

	/**
	 * 取得壓縮檔串流(傳入檔案清單)
	 */
	public static ByteArrayOutputStream getZipFileByteArrayOutputStream(File[] fileList) throws Exception {
		// 壓縮檔案
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		byte[] buffer = new byte[1024];

		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			FileInputStream fis = new FileInputStream(file);
			zos.putNextEntry(new ZipEntry(file.getName()));

			int length;
			while ((length = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, length);
			}

			zos.closeEntry();
			fis.close();
		}
		zos.flush();
		zos.close();

		return baos;
	}

	/**
	 * 解Zip檔
	 */
	public static void decompressZipFile(String sourFileName, String targetFolderName) throws Exception {
		File srcFile = new File(sourFileName);
		decompressZipFile(srcFile, targetFolderName);
	}

	/**
	 * 解Zip檔
	 */
	public static void decompressZipFile(File srcFile, String targetFolderName) throws Exception {
		ZipFile zipFile = new ZipFile(srcFile);
		Enumeration enumeration = zipFile.entries();

		while (enumeration.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) enumeration.nextElement();

			if (entry.isDirectory()) {
				System.out.println("Extracting directory: " + entry.getName());
				(new File(targetFolderName + File.separator + entry.getName())).mkdir();
				continue;
			}

			System.out.println("Extracting file: " + entry.getName());

			File unzipFile = new File(targetFolderName + File.separator + entry.getName());
			File parent = unzipFile.getParentFile();
			if (parent != null && (!parent.exists())) {
				parent.mkdirs();
			}

			InputStream in = zipFile.getInputStream(entry);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(unzipFile));

			byte[] buffer = new byte[1024];
			int len;

			while ((len = in.read(buffer)) >= 0)
				out.write(buffer, 0, len);

			in.close();
			out.close();
		}

		zipFile.close();
	}

}
