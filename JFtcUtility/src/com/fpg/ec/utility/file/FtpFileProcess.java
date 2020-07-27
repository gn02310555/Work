package com.fpg.ec.utility.file;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPFile;

import com.fpg.ec.utility.StringUtil;
import com.fpg.ec.utility.Ystd;

/**
 * FTP檔案作業處理
 */
public class FtpFileProcess{
	
	/**
	 * 將資料上傳至FTP(批次作業名稱、FTP IP、FTP帳號、FPT密碼、使用者端上傳檔案位置、上傳檔案FTP儲存檔案位置)
	 */
	public static void sendFileToFtp(String iBatchName, String iFtpHost, String iFtpId, String iFtpPw, String iUploadFileLocalPath, String iUploadFileFtpPath) throws Exception {
		StringUtil stringUtil = new StringUtil();
			
		System.out.println(iBatchName + " sendFileToFtp start ........ Time:" + stringUtil.toFormat(new Ystd().utime(), "####/##/## ##:##:##"));

		try {
			File file = new File(iUploadFileLocalPath);
			File[] flList = file.listFiles();
			
			if (flList != null && flList.length > 0) {
				System.out.println("File Count:" + flList.length);
			}else{
				System.out.println("File Count:0");
			}

			if (flList != null && flList.length > 0) {
				// 整理是否有FileOK，沒有FileOK才可上傳
				boolean isCanUpload = true;
				
				for (int i = 0; i < flList.length; i++) {
					String strFile = flList[i].getName();
					if (strFile.toUpperCase().indexOf(".FILEOK") >= 0) {
						isCanUpload = false;
						break;
					}
				}
				
				if(isCanUpload){
					FtpWrapper ftp = new FtpWrapper();
					
					try {
						if (ftp.connectAndLogin(iFtpHost, iFtpId, iFtpPw)) {
							System.out.println("Connected to " + iFtpHost);
							
							ftp.setPassiveMode(true);
							ftp.binary();
							for (int i = 0; i < flList.length; i++) {
								String strFile = flList[i].getName();
								if (strFile.toUpperCase().indexOf(".FILEOK") < 0) {
									ftp.uploadFile(iUploadFileLocalPath + strFile, iUploadFileFtpPath + strFile);
									System.out.println("UpLoadFile :" + strFile);
								}
							}
							
							// 上傳完畢寫入OK檔
							new FileProcessor().samFile(iUploadFileLocalPath, iBatchName + ".FILEOK", new ArrayList(), "Big5");

						}

					} catch (Exception ftpe) {
						ftpe.printStackTrace();
						throw ftpe;
					} finally {
						ftp.logout();
						ftp.disconnect();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
		}

		System.out.println(iBatchName + " sendFileToFtp end ........ Time:" + stringUtil.toFormat(new Ystd().utime(), "####/##/## ##:##:##"));
	}

	/**
	 * 將FTP資料下載至主機資料夾(批次作業名稱、FTP IP、FTP帳號、FPT密碼、使用者端下載檔案位置、下載檔案FTP儲存檔案位置)
	 */
	public static int getFileFromFtp(String iBatchName, String iFtpHost, String iFtpId, String iFtpPw, String iDownLoadFileLocalPath, String iDownLoadFtpPath) throws Exception {
		StringUtil stringUtil = new StringUtil();
		int fileCnt = 0;
		
		System.out.println(iBatchName + "  getFileFromTCatFtp start ........ Time:" + stringUtil.toFormat(new Ystd().utime(), "####/##/## ##:##:##"));

		try {

			String strHost = iFtpHost;
			String strId = iFtpId;
			String strPwd = iFtpPw;
			String strDownLoadFtpPath = iDownLoadFtpPath;
			String strDownLoadLoacalPath = iDownLoadFileLocalPath;

			FtpWrapper ftp = new FtpWrapper();
			if (ftp.connectAndLogin(strHost, strId, strPwd)) {
				System.out.println("Connected to " + strHost);
				
				try {
					ftp.setPassiveMode(true);
					FTPFile[] files = ftp.listFiles(strDownLoadFtpPath);
					ftp.binary();

					if (files != null && files.length > 0) {
						System.out.println("File Count:" + files.length);
						fileCnt = files.length;
					}else{
						System.out.println("File Count:0");
					}
					
					// Download File
					for (int i = 0; i < files.length; i++) {
						String strFile = files[i].getName();
						
						if(!strFile.equals(".") && !strFile.equals("..")){
							ftp.downloadFile(strDownLoadFtpPath + strFile, strDownLoadLoacalPath + strFile);
							System.out.println("Get File :" + strFile);
							ftp.deleteFile(strDownLoadFtpPath + strFile);
							System.out.println("Delete File : " + strFile);
						}
					}

				} catch (Exception ftpe) {
					ftpe.printStackTrace();
					throw ftpe;
				} finally {
					ftp.logout();
					ftp.disconnect();
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
		}

		System.out.println(iBatchName + " getFileFromTCatFtp end ........ Time:" + stringUtil.toFormat(new Ystd().utime(), "####/##/## ##:##:##"));
		
		return fileCnt;
	}
	
	/**
	 * 在欄位值之間插入分隔符號，並轉為字串
	 */
	public static String insertTokenAndConvertToString(ArrayList<String> columnValues, String token) {
		StringBuilder resultTemp = new StringBuilder();
		int size = columnValues.size();
		for (int i = 0; i < size; i++) {

			resultTemp.append(columnValues.get(i));
			if ((i + 1) != size) {
				resultTemp.append(token);
			}
		}
		return resultTemp.toString();
	}
}
