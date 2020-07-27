package com.fpg.ec.utility.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fpg.ec.utility.StringUtil;

/**
 * @author PC18
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ZipUtil {

	/**
	 * Constructor for ZipUtil.
	 */
	public ZipUtil() {
		super();
	}

	/**
	 * 將某個檔案壓縮成ZIP檔
	 * @param i_filename : 含路徑的完整檔名
	 * @return String : 壓縮產生的ZIP檔名
	 */
	public String ZipFile(String i_filename) throws Exception {
		ZipOutputStream zipout = null;
		
		File fileSource = new java.io.File(new StringUtil().replaceWord(i_filename,"\\","/"));
		String strPreFilename = i_filename.substring(0,i_filename.lastIndexOf('.'));

		try {
			zipout = new ZipOutputStream(new FileOutputStream(strPreFilename + ".$$$"));

			FileInputStream in = null;
			try {
				in = new FileInputStream(fileSource);
				int b = -1;
				b = in.read();
				zipout.putNextEntry(new ZipEntry(fileSource.getName()));
				while (b >= 0) {
					zipout.write(b);
					b = in.read();
				}
				in.close();
			} catch (Exception e) {
				in.close();
				throw e;
			}
			zipout.closeEntry();
			zipout.close();
			
			//delete last generated zip file ...
            File fileTemp = new java.io.File(strPreFilename + ".zip");
            fileTemp.delete();
            //rename temp file to zip file ....
            fileTemp = new java.io.File(strPreFilename +".$$$");
            fileTemp.renameTo(new java.io.File(strPreFilename + ".zip"));
            
            return new StringUtil().replaceWord(fileTemp.getAbsolutePath(),"\\","/");
		} catch (Exception e) {
			throw e;
		}
	}

}
