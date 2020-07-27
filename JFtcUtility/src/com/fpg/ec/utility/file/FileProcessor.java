package com.fpg.ec.utility.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * 檔案處理Processor
 *
 */
public class FileProcessor {

	// 宣告 BufferedWriter
	private BufferedWriter writer = null;

	/**
	 * 寫檔(Encoding:UTF-8 or Big5)
	 */
	public void samFile(String i_FilePath, String i_FileName, ArrayList i_alData, String i_Encoding) throws Exception {
		try {
			int iSize = 0;
			if (i_alData != null) {
				iSize = i_alData.size();
			}

			// 假如 無此目錄 建立目錄
			File file = new File(i_FilePath);
			file.mkdir();

			// 刪除上次轉出檔案
			file = new File(i_FilePath + i_FileName);
			file.delete();

			// BufferedWriter 的檔案
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(i_FilePath + i_FileName, true), i_Encoding));

			// 寫入檔案
			for (int i = 0; i < iSize; i++) {
				writer.write((String) i_alData.get(i));
				writer.newLine();
			}
			i_alData.clear();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * 取得某資料夾下的所有檔案名稱
	 * 2010/11/16 dashontsai modified for 須依時間先後順序排序
	 */
	public ArrayList getFileList(String i_path, boolean i_isfullpath) throws Exception {
		ArrayList alFile = new ArrayList();
		
		try {
			File flTmpFolder = new File(i_path);
			File[] flList = flTmpFolder.listFiles();
			
			if (flList != null && flList.length > 0) {
				// 須依時間先後順序排序
				for (int i = 0; i < flList.length; i++) {
					for (int j = (i + 1); j < flList.length; j++) {
						File tempFile = null;
						
						String iFileName = flList[i].getName();
						iFileName = getAdjustedFileName(iFileName);
						String jFileName = flList[j].getName();
						jFileName = getAdjustedFileName(jFileName);
						
						if (iFileName.compareTo(jFileName) > 0) {
							tempFile = flList[i];
							flList[i] = flList[j];
							flList[j] = tempFile;
						}
					}
				}
				
				for (int i = 0; i < flList.length; i++) {
					File flTemp = flList[i];
					
					if (i_isfullpath) {
						alFile.add(i_path + flTemp.getName());
					} else {
						alFile.add(flTemp.getName());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			alFile = new ArrayList();
		}

		return alFile;

	}

	/**
	 * 移動檔案
	 */
	public String moveFile(String i_sourcefile, String i_tagetfile) throws Exception {
		InputStream in = null;
		OutputStream out = null;
		File fileSource = new File(i_sourcefile);
		File fileTaget = new File(i_tagetfile);
		try {
			in = new FileInputStream(fileSource);
			fileTaget.createNewFile();
			out = new FileOutputStream(fileTaget);
			int i = in.read();
			while (i >= 0) {
				out.write(i);
				i = in.read();
			}
			out.flush();
			out.close();
			in.close();

			fileSource.delete();

			return fileTaget.getName();
		} catch (Exception e) {
			e.printStackTrace();
			fileTaget.delete();
			throw new Exception("move file error !!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e1) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e1) {
				}
			}
		}
	}
	
	/**
	 * 取得調整後檔名(由於日期放置後方 所以須將日期往前搬)
	 */
	private String getAdjustedFileName(String iFileName) {
		String strAdjustedFileName = iFileName;
		
		// 日期字串有六位數(西元後兩位+月份(兩位)+日期(兩位))
		int intDateLength = 6;
		
		for (int i = 0; i <= (strAdjustedFileName.length() - intDateLength); i++) {
			String strDateStr = strAdjustedFileName.substring(i, (i + intDateLength));
			
			// 日期全部為數字
			if (strDateStr.matches("^\\d+$")) {
				strAdjustedFileName = strDateStr + strAdjustedFileName.replaceAll(strDateStr, "");
				break;
			}
		}
		
		return strAdjustedFileName;
	}
	
}