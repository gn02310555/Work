package com.fpg.ec.utility.file;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.fpg.ec.utility.StringUtil;

public class FileUtil {
	/**
	* <pre><code>
	* Creation date: (2002/1/8 下午 03:34:31)
	* @author: C S Cheng
	* @return java.lang.String
	* @param1 要轉換的檔案 (須 full path) 
	* @param  HashMap 放要置換的內容
	* @HashMap Key前後規定為 && 如 &&user&&
	* Ex. filReplace("c:\\temp\\aaa.txt,hm)
	* </code></pre>
	*/
	public String replaceWord(String i_file, java.util.HashMap i_hm) {
		//FileReader fr = null; //20040206
		FileInputStream fi = null;
		int cnt = i_hm.size(); //  No. of replace
		String s = "";
		String[] srch = new String[cnt];
		String[] sub = new String[cnt];
		StringBuffer sb = new StringBuffer();
		Set set = i_hm.keySet();
		Iterator it = set.iterator();
		int j = 0;
		while (it.hasNext()) {
			srch[j] = (String) it.next();
			sub[j] = (String) i_hm.get(srch[j]);
			j++;
		}
		// 
		try {
			//fr = new FileReader(i_file); //20040206
			fi = new FileInputStream(i_file);
			
			
			//BufferedReader br = new BufferedReader(fr); //20040206
			//BufferedReader br = new BufferedReader(new InputStreamReader(fi,"big5"));
			BufferedReader br = new BufferedReader(new InputStreamReader(fi));
			while ((s = br.readLine()) != null) {
				sb.append(s);
			} //end  try
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//fr.close(); //20040206
				fi.close();
			} catch (Exception e1) {
			}
		}
		//
		String org = sb.toString();
		String rst = "";
		for (int i = 0; i < cnt; i++) {
			rst = new StringUtil().replaceWord(org, srch[i], sub[i]);
			org = rst;
		}
		return rst;
	}

	/**
	* <pre><code>
	* Creation date: (2002/1/8 下午 03:34:31)
	* @author: C S Cheng
	* @return java.lang.String
	* @param1 要轉換的檔案 (須 full path) 
	* @param  HashMap 放要置換的內容
	* @HashMap Key前後規定為 && 如 &&user&&
	* Ex. filReplace("c:\\temp\\aaa.txt,hm)
	* </code></pre>
	*/
	public String replaceWordBig5(String i_file, java.util.HashMap i_hm) {
		//FileReader fr = null; //20040206
		FileInputStream fi = null;
		int cnt = i_hm.size(); //  No. of replace
		String s = "";
		String[] srch = new String[cnt];
		String[] sub = new String[cnt];
		StringBuffer sb = new StringBuffer();
		Set set = i_hm.keySet();
		Iterator it = set.iterator();
		int j = 0;
		while (it.hasNext()) {
			srch[j] = (String) it.next();
			sub[j] = (String) i_hm.get(srch[j]);
			j++;
		}
		// 
		try {
			//fr = new FileReader(i_file); //20040206
			fi = new FileInputStream(i_file);
			
			
			//BufferedReader br = new BufferedReader(fr); //20040206
			BufferedReader br = new BufferedReader(new InputStreamReader(fi,"big5"));
			while ((s = br.readLine()) != null) {
				sb.append(s);
			} //end  try
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//fr.close(); //20040206
				fi.close();
			} catch (Exception e1) {
			}
		}
		//
		String org = sb.toString();
		String rst = "";
		for (int i = 0; i < cnt; i++) {
			rst = new StringUtil().replaceWord(org, srch[i], sub[i]);
			org = rst;
		}
		return rst;
	}

	/**
	* <pre><code>
	* Creation date: (2002/1/8 下午 03:34:31)
	* @author: C S Cheng
	* @return java.lang.String
	* @param1 要轉換的檔案 (須 full path) 
	* @param  HashMap 放要置換的內容
	* @HashMap Key前後規定為 && 如 &&user&&
	* Ex. filReplace("c:\\temp\\aaa.txt,hm)
	* </code></pre>
	*/
	public String replaceWordUtf8(String i_file, java.util.HashMap i_hm) {
		//FileReader fr = null; //20040206
		FileInputStream fi = null;
		int cnt = i_hm.size(); //  No. of replace
		String s = "";
		String[] srch = new String[cnt];
		String[] sub = new String[cnt];
		StringBuffer sb = new StringBuffer();
		Set set = i_hm.keySet();
		Iterator it = set.iterator();
		int j = 0;
		while (it.hasNext()) {
			srch[j] = (String) it.next();
			sub[j] = (String) i_hm.get(srch[j]);
			j++;
		}
		// 
		try {
			//fr = new FileReader(i_file); //20040206
			fi = new FileInputStream(i_file);
			
			
			//BufferedReader br = new BufferedReader(fr); //20040206
			BufferedReader br = new BufferedReader(new InputStreamReader(fi,"utf8"));
			while ((s = br.readLine()) != null) {
				sb.append(s);
			} //end  try
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//fr.close(); //20040206
				fi.close();
			} catch (Exception e1) {
			}
		}
		//
		String org = sb.toString();
		String rst = "";
		for (int i = 0; i < cnt; i++) {
			rst = new StringUtil().replaceWord(org, srch[i], sub[i]);
			org = rst;
		}
		return rst;
	}

	/**
	 * 將ArrayList資料依序寫入指定的檔案
	 * @param i_filename : 含路徑的完整檔名
	 * @param i_data : 欲寫入的資料
	 */
	public void writeFile(String i_filename, ArrayList i_data) throws Exception {

		int intDataSize = i_data.size();
		try {
			File fileTemp = new File(i_filename);
			fileTemp.delete();
			
			File filePath = new File(fileTemp.getParent());
			filePath.mkdir();
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(i_filename, true), "Big5"));

			//寫入檔案
			for (int i = 0; i < intDataSize; i++) {
				writer.write((String) i_data.get(i));
				//writer.newLine();
				writer.write("\r\n");
			}
            writer.close();
		} catch (Exception ex) {
			throw ex;
		}

	}
}