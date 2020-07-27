package com.fpg.ec.utility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringUtil {

	public String replaceWord(String strSource, String strWord, String strNewWord) {
		StringBuffer strbReturn = new StringBuffer();
		if (strSource == null || strSource.length() == 0)
			return "";
		int intStartOffset = 0;
		for (int intEndOffset = 0; intEndOffset >= 0 && intEndOffset < strSource.length();) {
			intEndOffset = strSource.indexOf(strWord, intEndOffset);
			if (intEndOffset >= 0) {
				String strTest = strSource.substring(intStartOffset, intEndOffset);
				strbReturn.append(strSource.substring(intStartOffset, intEndOffset)).append(strNewWord);
				intStartOffset = intEndOffset + strWord.length();
				intEndOffset++;
			}
		}

		strbReturn.append(strSource.substring(intStartOffset));
		return strbReturn.toString();
	}

	public String replaceHtmlSpecChar(String strSource) {
		String strReturn = strSource;
		if (strReturn == null)
			return "";
		//取代'&'為'&amp;'
		//strReturn = replaceWord(strReturn, "&", "&amp;");
		//取代'<'為'&lt;'
		strReturn = replaceWord(strReturn, "<", "&lt;");
		//取代'>'為'&gt;'
		strReturn = replaceWord(strReturn, ">", "&gt;");
		//取代'"'為'&quot;'
		strReturn = replaceWord(strReturn, "\"", "&quot;");
		return strReturn;
	}

	/**
	 * <pre><code>
	 * 利用亂數產生密碼。
	 * i_num:密碼位數。
	 * </code></pre>
	 */
	public String makPswd(int i_num) {
		String strPswd = null;
		boolean obj = false;
		//驗證密碼需符合複雜度
		do{
			strPswd = genPswd(i_num);
			obj = validatePswd(strPswd);
		}while(!obj);
		
		return strPswd;
	}
	
	/**
	 * 利用亂數產生密碼。
	 * @param 密碼位數
	 * @return
	 */
	private String genPswd(int i_num) {
		//列出所有數字、大小寫英文，隨機選
		String strLetter = "0123456789QAZWSXEDCRFVTGBYHNUJMIKOLPqazwsxedcrfvtgbyhnujmikolp";
		StringBuffer strPswd = new StringBuffer();
		int k = 0;
		for (int i = 1; i <= i_num; i++) {
			k = Math.round(Math.round(Math.random() * 1000) % strLetter.length());
			strPswd = strPswd.append(strLetter.charAt(k));
		}
		return strPswd.toString();
	}
	
	/**
	 * 驗證密碼複雜度。
	 * pattern1. 數字至少一位。
	 * pattern2. 大寫英文至少一位。
	 * pattern3. 小寫英文至少一位。
	 */
	public boolean validatePswd(String strPswd){
		boolean obj = false;
		
		String pattern1 = ".*[0-9]{1,}.*";
		String pattern2 = ".*[A-Z]{1,}.*";
		String pattern3 = ".*[a-z]{1,}.*";
		boolean result1 = strPswd.toString().matches(pattern1);
		boolean result2 = strPswd.toString().matches(pattern2);
		boolean result3 = strPswd.toString().matches(pattern3);
		
		if(result1 && result2 && result3){
			obj = true;
		}
		return obj;
	}

	/**
	 * 輸入字串 text 與經MD5 or SHA演算法計算過後的字串進行比對。
	 * @return   boolean ==> 是否相同，true: 表示相同。
	 * @param text java.lang.String 輸入字串。
	 * @param cipher java.lang.String 加密後字串。
	 */
	public boolean compareMD5(String text, String cipher) {
		String code = doMD5(text);
		if (code.equals(cipher)) {
			return true;
		} else {
			return false;
		}
	}

	public String replaceBackHtmlSpecChar(String strSource) {
		String strReturn = strSource;
		if (strReturn == null)
			return "";
		//取代'&amp;'為'&'
		strReturn = replaceWord(strReturn, "&amp;", "&");
		//取代'&lt;'為'<'
		strReturn = replaceWord(strReturn, "&lt;", "<");
		//取代'&gt;'為'>'
		strReturn = replaceWord(strReturn, "&gt;", ">");
		//取代'&quot;'為'"'
		strReturn = replaceWord(strReturn, "&quot;", "\"");
		return strReturn;
	}

	/**
	 *  輸入字串與經SHA演算法計算過後的字串與密碼，
	 *  進行比對。
	 * @return   boolean ==> 是否相同，true: 表示相同。
	 * @param text java.lang.String 輸入字串。
	 * @param cipher java.lang.String 密碼。
	 */
	public boolean compareSHA(String text, String cipher) {
		String code = doSHA(text);
		if (code.equals(cipher)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <pre><code>
	 * 將字串i_data 利用i_algo(MD5 or SHA algorithm)做hash 運算。
	 * @return java.lang.String。
	 * @param i_data java.lang.String。   
	 * @param i_algo java.lang.String。
	 * 字串經Hash演算法運算後，可得到唯一的結果值，該值無法，
	 * 經由反運算得到原來的明碼字串，再經BASE64轉碼為字元，以保
	 * 存在資料庫Table中。
	 * </code></pre>
	 */
	public String doDigest(String i_data, String i_algo) {
		String msgDig = new String();
		// 以 i_algo 變數決定使用的algorithm為 MD5 或 SHA
		try {
			byte[] buffer = i_data.getBytes("ISO-8859-1");
			MessageDigest md = MessageDigest.getInstance(i_algo);
			md.update(buffer);
			byte[] output = md.digest();
			// 轉成字串
			BASE64Encoder encode = new BASE64Encoder();
			msgDig = encode.encode(output);
		} catch (Exception e) {
			msgDig = "";
		}
		return msgDig;
	}

	/**
	 * <pre><code>
	 * 將字串 msg 做 MD5 Hash 運算。
	 * @return java.lang.String。
	 * @param msg java.lang.String。
	 * </code></pre>
	 */
	public String doMD5(String msg) {
		return doDigest(msg, "MD5");
	}

	/**
	 * <pre><code>
	 * 將字串 msg 做 SHA Hash 運算。
	 * @return java.lang.String。
	 * @param msg java.lang.String。
	 * </code></pre>
	 */
	public String doSHA(String msg) {
		return doDigest(msg, "SHA");
	}

	/**
	 * 將字串套用格式輸出
	 * @param i_datetime : 字串
	 * @param i_format : 格式
	 */
	public String toFormat(String i_datetime, String i_format) {
		i_format = i_format.toLowerCase();
		StringBuffer strbReturn = new StringBuffer();

		//count the number of '#' in i_format
		int cntMark = 0;
		for (int i = 0; i < i_format.length(); i++) {
			if (i_format.charAt(i) == '#')
				cntMark++;
		}

		if (i_datetime == null || i_datetime.length() == 0) {
			return "";
		}

		//generate return string
		int pntSource = 0; //point to source string -- i_datetime
		for (int i = 0; i < i_format.length(); i++) {
			if (i_format.charAt(i) == '#') {
				strbReturn.append(i_datetime.charAt(pntSource));
				pntSource++;
				if (pntSource >= i_datetime.length())
					break;
			} else {
				strbReturn.append(i_format.charAt(i));
			}
		}
		return strbReturn.toString();
	}

	/**
	 * 分割字串為ArrayList
	 * @param i_source : 原始字串
	 * @param i_split : 分割字元
	 * @return ArrayList : 結果
	 */
	public ArrayList split(String i_source, String i_split) {
		StringTokenizer strtTemp = new StringTokenizer(i_source, i_split);
		ArrayList alReturn = new ArrayList();
		while (strtTemp.hasMoreElements()) {
			String strTemp = strtTemp.nextToken();
			alReturn.add(strTemp);
		}
		return alReturn;
	}
	
	/**
	 * 依據特定長度分割字串為ArrayList
	 * @param i_source : 原始字串
	 * @param i_splitLength : 分割特定長度
	 * @return ArrayList : 結果
	 */
	public ArrayList splitStrByLength(String i_source, int i_splitLength) {
		ArrayList alReturn = new ArrayList();
		
		StringBuffer strBuf = new StringBuffer();
		
		if(i_source.length() <= i_splitLength){
			alReturn.add(i_source);
		}else{
			int lenCnt = 0;
			for(int i= 0; i<i_source.length(); i++){
				strBuf.append(i_source.substring(i, i+1));
				lenCnt++;
	
				if(lenCnt >= i_splitLength){
					alReturn.add(strBuf.toString());
					strBuf.setLength(0);
					lenCnt = 0;
				}else if((i+1) >= i_source.length()){
					alReturn.add(strBuf.toString());
				}
			}
		}

		return alReturn;
	}

	/**
	 * 將字串套用格式輸出
	 * @param i_datetime : 字串
	 * @param i_format : 格式
	 * @param i_mark : 替代原字串識別字元
	 */
	public String toFormat(String i_datetime, String i_format, char i_mark) {
		i_format = i_format.toLowerCase();
		StringBuffer strbReturn = new StringBuffer();

		//count the number of mark in i_format
		int cntMark = 0;
		for (int i = 0; i < i_format.length(); i++) {
			if (i_format.charAt(i) == i_mark)
				cntMark++;
		}

		if (cntMark > i_datetime.length())
			return "";

		//generate return string
		int pntSource = 0; //point to source string -- i_datetime
		for (int i = 0; i < i_format.length(); i++) {
			if (i_format.charAt(i) == i_mark) {
				strbReturn.append(i_datetime.charAt(pntSource));
				pntSource++;
			} else {
				strbReturn.append(i_format.charAt(i));
			}
		}
		return strbReturn.toString();
	}

	/**
	 * 從左邊補足字元
	 * @param i_source : 原始字串
	 * @param i_len : 欲補足之長度
	 * @param i_char : 欲補之字元
	 * @return String : 結果字串
	 */
	public String lpad(String i_source, int i_len, char i_char) {
		StringBuffer strbTmp = new StringBuffer(i_source);
		strbTmp.reverse();
		int intOffset = i_len - i_source.length();
		for (int i = 0; i < intOffset; i++) {
			strbTmp.append(i_char);
		}
		return strbTmp.reverse().toString();
	}

	/**
	 * 從右邊補足字元
	 * @param i_source : 原始字串
	 * @param i_len : 欲補足之長度
	 * @param i_char : 欲補之字元
	 * @return String : 結果字串
	 */
	public String rpad(String i_source, int i_len, char i_char) {
		StringBuffer strbTmp = new StringBuffer(i_source);

		int intOffset = i_len - i_source.length();
		for (int i = 0; i < intOffset; i++) {
			strbTmp.append(i_char);
		}
		return strbTmp.toString();
	}

	public int lengthForDB(String i_source, boolean forUnicode) {
		int intReturn = 0;
		for (int i = 0; i < i_source.length(); i++) {
			char c = i_source.charAt(i);

			if ((Integer.toHexString((int) c).length() / 2) >= 2) { //全型字
				intReturn += ((forUnicode) ? 3 : 2);
			} else {
				intReturn += Integer.toHexString((int) c).length() / 2;
				intReturn += ((Integer.toHexString((int) c).length() % 2) == 0) ? 0 : 1;
			}
		}
		return intReturn;
	}

	/**
	 * 分割字串為ArrayList
	 * @param i_source : 原始字串
	 * @param i_cutlen : 分割長度
	 * @param forUnicode : 是否for unicode
	 * @return ArrayList : 結果
	 */
	public ArrayList split(String i_source, int i_cutlen, boolean forUnicode) {
		StringBuffer strbParagraph = new StringBuffer();
		int lenParagraph = 0;
		java.util.ArrayList alParagraph = new java.util.ArrayList();

		/*
		 byte[] b = i_source.getBytes();
		 
		 byte[] b2 = new byte[100];
		 int cnt = 0;
		 for(int i=6; i < 10; i++){
		 b2[cnt++] = b[i];
		 }
		 
		 new String(b2);
		 */

		for (int i = 0; i < i_source.length(); i++) {
			char c = i_source.charAt(i);

			int sinlen = 0;
			if ((Integer.toHexString((int) c).length() / 2) >= 2) { //全型字
				sinlen += ((forUnicode) ? 3 : 2);
			} else {
				sinlen = Integer.toHexString((int) c).length() / 2;
				sinlen += ((Integer.toHexString((int) c).length() % 2) == 0) ? 0 : 1;
			}

			if ((lenParagraph + sinlen) <= i_cutlen) {
				lenParagraph += sinlen;
				strbParagraph.append(c);

				if (i == i_source.length() - 1) {
					alParagraph.add(strbParagraph.toString());
				}
			} else {
				alParagraph.add(strbParagraph.toString());
				strbParagraph = new StringBuffer();
				lenParagraph = 0;
				strbParagraph.append(c);
				lenParagraph += sinlen;
			}
		}

		return alParagraph;
	}

	/**
	 * 移除字串中之html tag
	 * @param i_htmlstr : html 字串
	 * @return
	 */
	public String removeHtmlTag(String i_htmlstr) {
		StringBuffer strbReturn = new StringBuffer();
		boolean isInTag = false;
		int lenHtmlStr = i_htmlstr.length();
		for (int i = 0; i < lenHtmlStr; i++) {
			char c = i_htmlstr.charAt(i);
			if (c == '<') {
				isInTag = true;
				continue;
			}
			if (c == '>') {
				isInTag = false;
				continue;
			}

			if (isInTag)
				continue;

			strbReturn.append(c);
		}

		return strbReturn.toString();
	}

	/**
	 * 取得Single quote 成2個Single quote
	 * @param i_str
	 * @return
	 */
	public String replaceSingleQuote22SingleQuote(String i_str) {
		return replaceWord(i_str, "'", "''");
	}

	/**
	 * 轉成日期格式輸出
	 * @param i_datestr
	 * @param i_pattern
	 * @return 
	 */
	public String toDateFormat(String i_datestr, String i_pattern, Locale i_loc) {
		if (i_datestr.length() < 8) {
			return i_datestr;
		}
		int intYear = Integer.parseInt(i_datestr.substring(0, 4));
		int intMonth = Integer.parseInt(i_datestr.substring(4, 6)) - 1;//base 0
		int intDay = Integer.parseInt(i_datestr.substring(6, 8));
		Calendar c = Calendar.getInstance();
		c.set(intYear, intMonth, intDay);
		if (intYear != c.get(Calendar.YEAR) || intMonth != c.get(Calendar.MONTH) || intDay != c.get(Calendar.DATE)) {
			throw new RuntimeException("日期格式錯誤");
		}
		SimpleDateFormat formatter = new SimpleDateFormat(i_pattern, i_loc);
		return formatter.format(c.getTime());
	}

	/**
	 * 日期轉為Locale(en,US)之 
	 * (1) 8碼時 ==> MMMM dd, yyyy => October 10, 2008
	 * (2) 6碼時 ==> MMMM,  yyyy => October, 2008
	 * @param i_datestr
	 * @return
	 */
	public String toEnusDateFormat1(String i_datestr) {
		if (i_datestr == null || (i_datestr.trim().length() != 8 && i_datestr.trim().length() != 6)) {
			return i_datestr;
		}
		if (i_datestr.trim().length() == 6) {
			return toDateFormat(i_datestr + "01", "MMMM, yyyy", new Locale("en", "US"));
		}
		return toDateFormat(i_datestr, "MMMM dd, yyyy", new Locale("en", "US"));
	}

	/**
	 * 日期轉為Locale(en,US)之MM/dd/yyyy => 10/10/2008
	 * @param i_datestr
	 * @return
	 */
	public String toEnusDateFormat2(String i_datestr) {
		if (i_datestr == null || i_datestr.trim().length() != 8) {
			return i_datestr;
		}
		return toDateFormat(i_datestr, "MM/dd/yyyy", new Locale("en", "US"));
	}

	/**
	 * 日期時間轉為Locale(en,US)之MMMM dd, yyyy hh:mm:ss=> October 10, 2008 01:20:33
	 * @param i_dattmstr
	 * @return
	 */
	public String toEnusDattmFormat1(String i_dattmstr) {
		if (i_dattmstr == null || i_dattmstr.trim().length() != 14) {
			return i_dattmstr;
		}
		return toDateFormat(i_dattmstr, "MMMM dd, yyyy ", new Locale("en", "US")) + toFormat(i_dattmstr.substring(8), "##:##:##");
	}

	/**
	 * 日期時間轉為Locale(en,US)之MM/dd/yyyy hh:mm:ss=> October 10, 2008 01:20:33
	 * @param i_dattmstr
	 * @return
	 */
	public String toEnusDattmFormat2(String i_dattmstr) {
		if (i_dattmstr == null || i_dattmstr.trim().length() != 14) {
			return i_dattmstr;
		}
		return toDateFormat(i_dattmstr, "MM/dd/yyyy ", new Locale("en", "US")) + toFormat(i_dattmstr.substring(8), "##:##:##");
	}

	/**
	 * 金額轉為 (Locale)$ ###,###.## => 1,000.00
	 * @param i_dollarstr
	 * @param i_loc
	 * @param i_decimalnum : 小數位數
	 * @return
	 */
	public String toDollarFormat(String i_dollarstr, Locale i_loc, String i_decimalnum) {
		try {
			int intDecimalNum = Integer.parseInt(i_decimalnum);
			return toDollarFormat(i_dollarstr, i_loc, intDecimalNum);
		} catch (Exception e) {
			throw new RuntimeException("toDollarFormat error : i_dollarstr is (" + i_dollarstr + "),i_decimalnum is (" + i_decimalnum + ") ");
		}
	}
	
	/**
	 * 金額轉為 (Locale)$ ###,###.## => 1,000.00
	 * @param i_dollarstr
	 * @param i_loc
	 * @param i_decimalnum : 小數位數
	 * @return
	 */
	public String toDollarFormat(String i_dollarstr, String i_decimalnum) {
		try {
			int intDecimalNum = Integer.parseInt(i_decimalnum);
			return toDollarFormat(i_dollarstr, null, intDecimalNum);
		} catch (Exception e) {
			throw new RuntimeException("toDollarFormat error : i_dollarstr is (" + i_dollarstr + "),i_decimalnum is (" + i_decimalnum + ") ");
		}
	}

	/**
	 * 金額轉為 (Locale)$ ###,###.## => 1,000.00
	 * @param i_dollarstr
	 * @param i_loc
	 * @param i_decimalnum : 小數位數
	 * @return
	 */
	public String toDollarFormat(String i_dollarstr, Locale i_loc, int i_decimalnum) {
		
		try {
			
			if(i_dollarstr == null || i_dollarstr.length() ==0) {
				return "";
			}
			
			BigDecimal bdTemp = new BigDecimal(i_dollarstr);
			int intDecimalNum = i_decimalnum;
			StringBuffer strbPattern = new StringBuffer("###,###,###,###,###,##0");
			if (intDecimalNum > 0) {
				strbPattern = strbPattern.append("." + rpad("", intDecimalNum, '0'));
			}
			DecimalFormat df = new DecimalFormat(strbPattern.toString());

			String strDollarSignStr = "";
            if(i_loc == null) {
            	return df.format(bdTemp);
            }			
			if ("en_us".equalsIgnoreCase(i_loc.toString())) {
				return "US$" + df.format(bdTemp);
			}
			if ("zh_tw".equalsIgnoreCase(i_loc.toString())) {
				return "NT$" + df.format(bdTemp);
			}
			if ("ja_jp".equalsIgnoreCase(i_loc.toString())) {
				return df.format(bdTemp) + "&#20870";
			}

			return df.format(bdTemp);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("toDollarFormat error : i_dollarstr is (" + i_dollarstr + "),i_decimalnum is (" + i_decimalnum + ") ");
		}
	}

	/**
	 * 金額轉換  1000.00 => 1,000.00   1234 => 1,234
	 * @param i_dollarstr
	 * @return
	 */
	public String toDollarFormat(String i_dollarstr) {
		try {
			if(i_dollarstr == null || i_dollarstr.length() == 0) {
				return "";
			}
			
			if(Double.parseDouble(i_dollarstr) == 0){
				return "0";
			}

			BigDecimal bdTemp = new BigDecimal(i_dollarstr);			
			bdTemp = new BigDecimal(bdTemp.stripTrailingZeros().toPlainString());//處理SQL Server小數補0之問題
			
			StringBuffer strbPattern = new StringBuffer("###,###,###,###,###,##0.#####");
	
			DecimalFormat df = new DecimalFormat(strbPattern.toString());
			String strReturn= df.format(bdTemp);

			return strReturn;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("toDollarFormat error : i_dollarstr is (" + i_dollarstr + ")");
		}
	}
	
	
	/**
	 * 轉換日期格式
	 * @param i_dateStr : 日期字串
	 * @param i_objFormat : 原格式
	 * @param i_newFormat : 轉換格式
	 * @return
	 */
	public String convertDateStrFormat(String i_dateStr, String i_objFormat, String i_newFormat) throws Exception {

		SimpleDateFormat dateformatSpec = new SimpleDateFormat(i_objFormat);
		String dateStringToParse = i_dateStr;
		Date date = dateformatSpec.parse(dateStringToParse);

		if (!i_dateStr.equals(dateformatSpec.format(date))) {
			throw new Exception("Invalid date : (" + i_dateStr + ") with pattern (" + i_objFormat + ")");
		}

		SimpleDateFormat dateformatDefault = new SimpleDateFormat(i_newFormat);
		return dateformatDefault.format(date);
	}

	/**
	 * 功能:轉換含Unicode之文字
	 * @i_str1 java.lang.String 欲轉換文字
	 * 範例:
	 *  convertUnicodeField(王&#5049;傑)
	 *  傳出:王偉傑
	 **/
	public String convertUnicodeField(String i_str1) {
		String beStr = "&#";
		String endStr = ";";
		String strReturn = "";
		boolean blnDo = true;
		try {
			do {
				int intBeginStrPlace; //"%#"位置
				int intEndStrPlace; //";"位置
				intBeginStrPlace = i_str1.indexOf(beStr);
				if (intBeginStrPlace != -1) {
					blnDo = true;
					intEndStrPlace = i_str1.indexOf(endStr);
					if (intEndStrPlace != -1) {
						int intLen = i_str1.length();
						String str1 = i_str1.substring(0, intBeginStrPlace);
						String str2 = i_str1.substring(intBeginStrPlace + 2, intEndStrPlace);
						String str3 = i_str1.substring(intEndStrPlace + 1, i_str1.length());
						i_str1 = str1 + (char) Integer.parseInt(str2) + str3;
					} else {
						blnDo = false;
						//throw (new Exception("有'&#'卻沒有';'"));
					}
				} else {
					blnDo = false;
				}
			} while (blnDo == true);
			return i_str1;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	/**
	 * <PRE><CODE>
	 * 頁面數字欄顯示方式(整數及小數),提供外部建檔方式
	 * style建立於properties file.
	 * 建立欲顯示的樣式。 
	 * 範例︰style=5.2 代表整數5位小數2位。
	 * 規格︰根據style轉出pattern,上例為#####.##
	 *       若6.0 則pattern為######
	 * 建立日期： (2001/11/2 下午 01:34:33)
	 * @作者： PC01
	 * @return java.lang.String
	 * @param style java.lang.String
	 * </CODE></PRE> 
	 */
	public static String createPattern(String style) {

		String pattern = "";
		int x; //integer
		int y; //fraction
		int n = style.indexOf(".");

		if (n == -1) {
			x = Integer.parseInt(style);
			y = 0;
		} else {
			x = Integer.parseInt(style.substring(0, n));
			y = Integer.parseInt(style.substring(n + 1));
		}

		for (int i = 0; i < x; i++) {
			pattern = pattern + "#";
		}

		if (y != 0) {
			pattern = pattern + ".";
			for (int i = 0; i < y; i++) {
				pattern = pattern + "#";
			}
		}
		return pattern;
	}

	/**
	 * <PRE><CODE>
	 * 將數字欄位依pattern及小數位轉為顯示值。
	 * 範例︰dFormat(1234.56,"#####.#") 值為1234.6。
	 * 規格︰請於此處插入方法程式規格。
	 * 建立日期： (2001/11/2 上午 11:18:26)
	 * @作者： PC01
	 * @return java.lang.String
	 * @param pattern java.lang.String
	 *  </CODE></PRE> 
	 */
	public static String dFormat(double num, String pattern) {
		DecimalFormat df = new DecimalFormat(pattern);
		StringBuffer buf = new StringBuffer();
		df.format(num, buf, new FieldPosition(0));
		return buf.toString();
	}

	/**
	 * <PRE><CODE>
	 * 從style取出欲顯示的小數位數,供round method使用。
	 * 範例︰style=5.2 則小數位為2。
	 *       style=5   則小數位為0。
	 * 規格︰請於此處插入方法程式規格。
	 * 建立日期： (2001/11/2 下午 01:34:33)
	 * @作者： PC01
	 * @return java.lang.String
	 * @param style float
	 * </CODE></PRE> 
	 */
	public static String getFraction(String style) {
		String strFrac; //fraction
		int n = style.indexOf(".");
		if (n == -1) {
			strFrac = "0";
		} else {
			strFrac = style.substring(n + 1);
		}

		return strFrac;
	}
	
	public static int getFractionLength(String style) {
		String strFrac = getFraction(style);
		if (strFrac.equals("0")) {
			return 0;
		} else {
			return strFrac.length();
		}
	}
	
	public static String dollarDecFormat(String pattern, String decNum) {
		if(decNum.length() > 0){
			DecimalFormat df = new DecimalFormat(pattern);
			BigDecimal bdDecNum = new BigDecimal(decNum);
			
			return df.format(bdDecNum);
		}else{
			return "";
		}
	}
	
    /**
	 * 取得計價單位
	 */
		//	非台幣 換算時(換算後 允許小數2位)			
		//	原始供應報價的金額	 <--換算-->	SAP價格單位	SAP價格
		//	300								1		300
		//	30								1		30
		//	3								1		3
		//	0.3								1		0.3
		//	0.03							1		0.03
		//	0.003							1000	3
		//	0.0003							1000	0.3
		//	0.00003							1000	0.03
		//	0.000003						10000	0.03
	//非台幣
	public static int calPrcUnitNoTWD(String strPrice) {
		if (strPrice.length() > 0) {
			BigDecimal bdQty = new BigDecimal(strPrice);
			DecimalFormat formatter = new DecimalFormat("###########.######");
			
			String prcUnit = formatter.format(bdQty);
			int decDigitCnt = getFractionLength(prcUnit);
			if(decDigitCnt >= 0 && decDigitCnt<=2){
				return 1;
			}else if(decDigitCnt >= 3 && decDigitCnt<=5){
				return 1000;
			}else{
				return 10000;
			}
		}else{
			return 0;
		}
	}
	
		//	台幣 換算時 (換算後不允許有小數位)			
		//	原始供應報價的金額	 <--換算-->	SAP價格單位	SAP價格
		//	300								1		300
		//	30								1		30
		//	3								1		3
		//	0.3								1000	300
		//	0.03							1000	30
		//	0.003							1000	3
		//	0.0003							10000	3
		//	0.00003							10000	0.3
		//	0.000003						10000	0.03
	//台幣
	public static int calPrcUnitTWD(String strPrice) {
		if (strPrice.length() > 0) {
			BigDecimal bdQty = new BigDecimal(strPrice);
			DecimalFormat formatter = new DecimalFormat("###########.######");

			String prcUnit = formatter.format(bdQty);
			int decDigitCnt = getFractionLength(prcUnit);
			if(decDigitCnt == 0){
				return 1;
			}else if(decDigitCnt >= 1 && decDigitCnt<=3){
				return 1000;
			}else{
				return 10000;
			}
		}else{
			return 0;
		}
	}

	/**
	 * <pre><code>
	 * 利用亂數產生序號。
	 * i_num:序號位數。
	 * </code></pre>
	 */
	public String makSN(int i_num) {

		String strLetter = "0123456789";
		StringBuffer strPswd = new StringBuffer();
		int k = 0;
		for (int i = 1; i <= i_num; i++) {
			k = Math.round(Math.round(Math.random() * 1000) % 10);
			strPswd = strPswd.append(strLetter.charAt(k));
		}
		return strPswd.toString();
	}

	/**
	 * <pre><code>
	 * Creation date: (2001/7/25 上午 10:45:47)
	 * @return double       四捨五入後的結果
	 * @param i_num double  要四捨五入的數值
	 * @param x int         取小數點後x位第x+1位四捨五入
	 * </code></pre>
	 */
	public double round(double i_num, int x) {
		double d, y;
		int n = 1;
		for (int i = 1; i <= x; i++)
			n = n * 10;
		y = n * 1.0;
		d = i_num * n;
		return (Math.round(d)) / y;
	}

	/**
	 * <pre><code>
	 * Creation date: (2001/7/25 上午 10:45:47)
	 * @return double       四捨五入後的結果
	 * @param i_num double  要四捨五入的數值
	 * @param x int         取小數點後x位第x+1位四捨五入
	 * </code></pre>
	 */
	public float round(float i_num, int x) {
		float d, y;
		int n = 1;
		for (int i = 1; i <= x; i++)
			n = n * 10;
		y = n * 1.0f;
		d = i_num * n;
		return (Math.round(d)) / y;
	}

	/**
	 * <pre><code>
	 * Creation date: (2002/1/7 下午 02:12:10)
	 * 功能:置換字串
	 * @param1 org java.lang.String
	 * @param2 要被置換之字串 java.lang.String
	 * @param3 新置換字串 java.lang.String
	 * @return java.lang.String
	 * Ex. strReplace("abcdefg","cd","123")
	 *     將字串"abcdefg"的"cd"置換成"123" return "ab123efg"
	 *     2003/05/15 修改成整篇文章只搜尋一次,解決無窮迴圈的問題
	 * </code></pre>
	 */
	public String strReplace(String org, String search, String sub) {
		String result = "";
		String resultall = "";
		int i;
		do { // replace all matching substrings
			i = org.indexOf(search);
			if (i != -1) {
				result = result + org.substring(0, i);
				result = result + sub;
				org = org.substring(i + search.length());
			}
		} while (i != -1);
		result = result + org;
		return result;
	}

	/**
	 * 擷取 text 字串中 subtext起往前loffset bytes,往後roffset bytes,
	 * 如終止字元非 blank , .等則往前(後)延伸，確保取得完整的word。
	 * Creation date: (2005/7/1 下午 01:13:41)
	 */
	public String extSentence(String text, String subtext, int loffset, int roffset) {
		int i = text.indexOf(subtext);
		int k, sptr, eptr;
		sptr = i - loffset >= 0 ? i - loffset : 0;
		if (sptr > 0) {
			for (k = sptr; k >= 1; k--) {
				String s = text.substring(k - 1, k);
				if (s.equals(" ") || s.equals(",") || s.equals(".") || s.equals("，") || s.equals("。"))
					break;
			}
		} else {
			k = 0;
		}
		//    
		int m;
		int len = text.length();
		eptr = i + roffset > len ? len : i + roffset;
		if (eptr < len) {
			for (m = eptr; m <= len; m++) {
				String s = text.substring(m - 1, m);
				if (s.equals(" ") || s.equals(",") || s.equals(".") || s.equals("，") || s.equals("。"))
					break;
			}
		} else
			m = len;
		return text.substring(k, m - 1);
	}

	/**
	 * 擷取 text 字串中 subtext起往前loffset bytes,往後roffset bytes,
	 * 如終止字元非 blank , .等則往前(後)延伸，確保取得完整的word。
	 * Creation date: (2005/7/1 下午 01:13:41)
	 */
	public String extSentenceByFBC(String text, String subtext, int loffset, int roffset) {
		int i = text.indexOf(subtext);
		int k, sptr, eptr;
		sptr = i - loffset >= 0 ? i - loffset : 0;
		if (sptr > 0) {
			for (k = sptr; k >= 1; k--) {
				String s = text.substring(k - 1, k);
				if (s.equals(" ") || s.equals(",") || s.equals(".") || s.equals("，") || s.equals("。"))
					break;
			}
		} else {
			k = 0;
		}
		//    
		int m;
		int len = text.length();
		eptr = i + roffset > len ? len : i + roffset;
		if (eptr < len) {
			for (m = eptr; m <= len; m++) {
				String s = text.substring(m - 1, m);
				if (s.equals(" ") || s.equals(",") || s.equals(".") || s.equals("，") || s.equals("。"))
					break;
			}
		} else
			m = len;
		String rst = text.substring(k, m - 1);
		int e1 = rst.indexOf(subtext);
		int l = subtext.length();
		//syli 940803 added
		if (e1 == -1) {
			e1++;
		}
		String p1 = rst.substring(0, e1);
		String p2 = rst.substring(e1 + l, rst.length());
		return p1 + "<FONT style='BACKGROUND-COLOR: #66ff66'>" + subtext + "</font>" + p2;
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
	 * 2003.06.12 Modify File reader--> FileInputStream(fis,"Big5")
	 * </code></pre>
	 */
	public String fileReplace(String i_file, java.util.HashMap i_hm) {
		FileReader fr = null;
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
			FileInputStream fis = new FileInputStream(i_file);
			InputStreamReader reader = new InputStreamReader(fis, "Big5");
			BufferedReader br = new BufferedReader(reader);
			while ((s = br.readLine()) != null) {
				sb.append(s);
			} //end  try
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (Exception e1) {
			}
		}
		//
		String org = sb.toString();
		String rst = "";
		for (int i = 0; i < cnt; i++) {
			rst = strReplace(org, srch[i], sub[i]);
			org = rst;
		}
		return rst;
	}

	public static void showClassMsg(Object i_class, String i_val1) {
		System.out.println(i_class.getClass().getName() + ":" + i_val1);
	}

	public static void showClassMsg(Object i_class, String i_val1, String i_val2) {
		System.out.println(i_class.getClass().getName() + ":" + i_val1 + "," + i_val2);
	}

	public static void showClassMsg(Object i_class, String i_val1, String i_val2, String i_val3) {
		System.out.println(i_class.getClass().getName() + ":" + i_val1 + "," + i_val2 + "," + i_val3);
	}

	public static void showClassMsg(Object i_class, String i_val1, String i_val2, String i_val3, String i_val4) {
		System.out.println(i_class.getClass().getName() + ":" + i_val1 + "," + i_val2 + "," + i_val3 + "," + i_val4);
	}

	public static void showClassMsg(Object i_class, String i_val1, String i_val2, String i_val3, String i_val4, String i_val5) {
		System.out.println(i_class.getClass().getName() + ":" + i_val1 + "," + i_val2 + "," + i_val3 + "," + i_val4 + "," + i_val5);
	}

	/**
	 * 產生XUID
	 * 以目前系統日期時間字串(YYYYMMDDHH24MISSsss)
	 * 轉以36進位表示
	 * @return
	 */
	public static String genXUID() {
		String strNow = Ystd.utime4();
		long longNow = Long.parseLong(strNow);
		try {
			Thread.sleep(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return to36CarryString(longNow);
	}

	public static String genXUID(int i) {
		return genXUID() + "_" + i;
	}

	public static String genXUID(String i) {
		return genXUID() + "_" + i;
	}

	/**
	 * 將long值轉以36進位字串表示
	 */
	public static String to36CarryString(long i_val) {
		return toNCarryString(i_val, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");

	}

	/**
	 * 將long值轉以字串表示
	 */
	public static String toNCarryString(long i_val, String i_code) {
		String strCode = i_code;
		int intCodeLen = strCode.trim().length();
		StringBuffer strbReturn = new StringBuffer();
		long longValue = i_val;
		for (;;) {
			long longQuotient = longValue / intCodeLen;
			int intRemainder = (int) (longValue % intCodeLen);

			strbReturn.append(strCode.charAt(intRemainder));
			longValue = longQuotient;
			if (longValue == 0) {
				break;
			}
		}

		return strbReturn.reverse().toString();
	}

	/**
	 * 轉換非big5字元為&#?????字符
	 * 例:(方方土) ==> &#22531;
	 */
	public String convertNonBig52UnicodeRealString(String i_org) {
		StringBuffer strNew = new StringBuffer();
		try {

			String strOrg = i_org;
			for (int i = 0; i < strOrg.length(); i++) {
				String strOneWord = strOrg.substring(i, i + 1);
				//System.out.println(strOneWord+":"+(int)strOneWord.charAt(0));
				//System.out.println("strOneWord:" + strOneWord);
				byte[] a = strOneWord.getBytes("BIG5");//特殊字時會變? ==> length = 1
				byte[] c = strOneWord.getBytes("UTF-8");//中文字時 ==> length = 3
				//System.out.println("a:" + a.length);
				/*
				boolean isBig5 = false;
				checkIsBig5: {
					if (a.length == 2) {
						int iHead = a[0] & 0xff;
						int iTail = a[1] & 0xff;
						isBig5 = ((iHead >= 0xa1 && iHead <= 0xf9 && (iTail >= 0x40 && iTail <= 0x7e || iTail >= 0xa1 && iTail <= 0xfe)) ? true : false);
					}
				}*/

				if ((a.length == 1 && c.length == 3)
				//|| ( !isBig5  && a.length == 2)
				) {
					strNew.append("&#");
					strNew.append((int) strOneWord.charAt(0));
					strNew.append(";");
				} else {
					strNew.append(strOneWord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strNew.toString();
	}
	
	/**
	 * 利用亂數產生識別碼(0-9a-zA-Z，總共62位)
	 * i_num:亂數位數。
	 * 2012/02/14 dashontsai modified
	 */
	public static String generateVcode(int i_num) {
		String strLetter = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sbVcode = new StringBuffer();
		int ran_index;
		for (int i = 0; i < i_num; i++) {
			ran_index = Math.round(Math.round(Math.random() * 1000) % strLetter.length());
			sbVcode.append(strLetter.charAt(ran_index));
		}
		return sbVcode.toString();
	}
	
	/**
	 * 正向區間取得Mask String
	 * 例:("abcdef", 1, 3, "*") ==> "a***ef"
	 */
	public String getMaskString(String source, int startIndex, int endIndex, String mask) {
		Hashtable indexHT = new Hashtable();
		
		for (int i = startIndex; i <= endIndex; i++) {
			indexHT.put(i + "", "*");
		}
		
		return getMaskString(source, indexHT, mask);
	}
	
	/**
	 * 反向區間取得Mask String
	 * 例:("abcdef", 0, 3, "*") ==> "ab****"
	 */
	public String getInverseMaskString(String source, int startIndex, int endIndex, String mask) {
		startIndex = source.length() - 1 - startIndex;
		endIndex = source.length() - 1 - endIndex;
	
		return getMaskString(source, endIndex, startIndex, mask);
	}
	
	/**
	 * 取得奇數Mask String
	 * 例:("abcdef", "*") ==> "*b*d*f"
	 */
	public String getOddMaskString(String source, String mask) {
		return getRemainderMaskString(source, 2, 1, mask);
	}
	
	/**
	 * 取得偶數Mask String
	 * 例:("abcdef", "*") ==> "a*c*e*"
	 */
	public String getEvenMaskString(String source, String mask) {
		return getRemainderMaskString(source, 2, 0, mask);
	}
	
	private String getRemainderMaskString(String source, int quotient, int remainder, String mask) {
		Hashtable indexHT = new Hashtable();
		
		for (int i = 0; i < source.length(); i++) {
			if (((i + 1) % quotient) == remainder) {
				indexHT.put(i + "", "*");
			}
		}
		
		return getMaskString(source, indexHT, mask);
	}

	private String getMaskString(String source, Hashtable indexHT, String mask) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < source.length(); i++) {
			String word = "";
			if (indexHT.containsKey(i + "")) {
				word = mask;
			} else {
				word = source.substring(i, i + 1);
			}
			sb.append(word);
		}
		
		return sb.toString();
	}
	
	/**
	 * 營利事業統一編號檢查程式
	 * reference: http://www.javaworld.com.tw/jute/post/view?bid=35&id=48650&sty=1&tpg=10&age=0
	 * 原則:
	 * 1. 必須為8位
	 * 2. 不能為00000000(八個零，非邏輯錯誤，但事實上不存在)
	 * 3. 假設統一編號為 A B C D E F G H 
	 * 		A - G 為編號, H 為檢查碼 
	 * 		A - G 個別乘上特定倍數, 若乘出來的值為二位數則將十位數和個位數相加 
	 * 		A x 1 
	 * 		B x 2 
	 * 		C x 1 
	 * 		D x 2 
	 * 		E x 1 
	 * 		F x 2 
	 * 		G x 4 
	 * 		H x 1 
	 * 		最後將所有數值加總, 被 10 整除就為正確 
	 * 		若上述演算不正確並且 G 為 7 得話, 再加上 1 被 10 整除也為正確 
	 */
	public static boolean checkRfno(String strRfno) {
        if (strRfno == null || strRfno.length() == 0) {
        	return false;
        }
        // 必須全部為數字(共八位)
        if (!strRfno.matches("^\\d{8}$")) {
        	return false;
        }
        // 不能為八個0
        if ("00000000".equals(strRfno)) {
        	return false;
        }
        
        int key[] = {1, 2, 1, 2, 1, 2, 4, 1};
        int temp = 0;
        int sum = 0;
        
        try{
            for (int i = 0; i < key.length; i++) {
                temp = Integer.parseInt(String.valueOf(strRfno.charAt(i))) * key[i];
                sum = sum + temp / 10 + temp % 10;
            }
            if ((sum % 10) == 0 
            		|| (strRfno.charAt(6) == '7' && ((sum + 1) % 10 == 0))
            		) {
            	return true;
            } else {
            	return false;
            }
        } catch(Exception e) {
            return false;
        }

	}
	
	/**
	 * 去除前置0
	 */
	public String trimPrefixZero(String originStr) {
		if (originStr == null || originStr.length() == 0) {
			return originStr;
		}
		String trimmedPrefixZeroStr = originStr.replaceAll("^0+", "");
		if (trimmedPrefixZeroStr.length() == 0) {
			return "0";
		}
		return trimmedPrefixZeroStr;
	}
	
	/**
	 * UUID產生識別碼-最大32位數
	 * i_num:位數。
	 * 20111107 cinhuei added
	 */
	public static String generateUUIDXuid(int i_num) {

		String strUUID = UUIDGenerator.getUUID(i_num);

		return strUUID;
	}
	
	/**
	 * UUID產生識別碼-最大32位數
	 * 20111107 cinhuei added
	 */
	public static String generateUUIDXuid() {

		String strUUID = UUIDGenerator.getUUID();

		return strUUID;
	}
	
	/**
	 * 取得序號每個位數加總
	 * 20111107 cinhuei added
	 */
	public static int getDigitValueSum(String strDigit) {
        if (strDigit == null || strDigit.length() == 0) {
        	return -1;
        }
        
        try{
            int sum = 0;
        	
            for (int i = 0; i < strDigit.length(); i++) {
                sum = sum + Integer.parseInt(String.valueOf(strDigit.charAt(i)));
            }
            
            System.out.println("sum:"+sum);
            return sum;
        } catch(Exception e) {
            return -1;
        }

	}
	
	/**
	 * 取得託運單號檢查碼
	 * 20111107 cinhuei added
	 * 	序號									
		位數	1	2	3	4	5	6	7	8	9	
		託運單號碼	6	0	2	9	1	4	4	7	1	
		Step 1
		前9位數加總	6+0+2+9+1+4+4+7+1=34									
		Step 2
		Step1數字
		除7取餘數	Mod(34,7)=6
		7*4=28
		34-28=6									
		Step 4
		檢查碼	34-28=6 ← result
		檢核碼為6
		如果餘數為0，檢核碼為0									
	 */
	public static int getConsignVerCode(String strConsignNum) {
        if (strConsignNum == null || strConsignNum.length() == 0) {
        	return -1;
        }
        
        int sumConsignNum = getDigitValueSum(strConsignNum);
        
        if (sumConsignNum == -1 || strConsignNum == null || strConsignNum.length() == 0) {
        	return -1;
        }
        
        try{
            int verCode = 0;
        	
            verCode = sumConsignNum % 7;
            
            System.out.println("verCode:"+verCode);
            return verCode;
        } catch(Exception e) {
            return -1;
        }

	}
	
	/**
	 * 將字串套用格式輸出
	 * @param i_datetime : 字串
	 * @param i_format : 格式
	 */
	public static String toFormatStatic(String i_datetime, String i_format) {
		i_format = i_format.toLowerCase();
		StringBuffer strbReturn = new StringBuffer();

		//count the number of '#' in i_format
		int cntMark = 0;
		for (int i = 0; i < i_format.length(); i++) {
			if (i_format.charAt(i) == '#')
				cntMark++;
		}

		if (i_datetime == null || i_datetime.length() == 0) {
			return "";
		}

		//generate return string
		int pntSource = 0; //point to source string -- i_datetime
		for (int i = 0; i < i_format.length(); i++) {
			if (i_format.charAt(i) == '#') {
				strbReturn.append(i_datetime.charAt(pntSource));
				pntSource++;
				if (pntSource >= i_datetime.length())
					break;
			} else {
				strbReturn.append(i_format.charAt(i));
			}
		}
		return strbReturn.toString();
	}
	
	/**
	 * 判斷是否為偶數								
	 */
	public static boolean chkIsEven(int iVal) {
		if(iVal % 2 == 0){
			//偶數
			return true;
		}else{
			//奇數
			return false;
		}
	}
	
	/**
	 * 判斷序號是否為Sparkle		
	 * 廠牌判斷原則
	 * SPK:
	 * 	  (1)序號長度為13碼且第1位碼為W
		  (2)序號長度為14碼且第一碼為W
		  (3)序號長度為13碼且第1位碼為C或S且第2位為數字
		  (4)序號長度為13碼且第1位碼為S且第4位為數字
		  (5)序號長度為12碼且第1,2位等於LG且該筆序號資料tblBarcode.PartNo或tblBarcode2.PartNo第4位等於S
	 * 非SPK:非上述條件者						
	 */
	public static boolean chkSerNoIsSpk(String iSerNo, String iPartNo) {
		if(iSerNo != null && iSerNo.length() > 0){
			if(((iSerNo.length() == 13) && (iSerNo.startsWith("W"))) || //序號長度為13碼且第1位碼為W
			   ((iSerNo.length() == 14) && (iSerNo.startsWith("W"))) || //序號長度為14碼且第一碼為W 
			   ((iSerNo.length() == 13) && (((iSerNo.startsWith("C") || iSerNo.startsWith("S")) && iSerNo.substring(1, 2).matches("\\d")))) || //序號長度為13碼且第1位碼為C或S且第2位為數字
			   ((iSerNo.length() == 13) && (iSerNo.startsWith("S") && iSerNo.substring(3, 4).matches("\\d"))) || //序號長度為13碼且第1位碼為S且第4位為數字
			   ((iSerNo.length() == 12) && ((iSerNo.startsWith("LG") && (iPartNo.length() > 0 && iPartNo.substring(3, 4).equals("S")))))	//序號長度為12碼且第1,2位等於LG且該筆序號資料tblBarcode.PartNo或tblBarcode2.PartNo第4位等於S
					){
				return true;
			}else{
				return false;
			}
		
		}else{
			return false;
		}
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
	
	/**
	 * 將Base64字串轉為Byte
	 * @param iStrBase64 : Base64字串
	 * @return byte[] : 結果
	 */
	public static byte[] decodeBase64(String iStrBase64) {  
	     byte[] bt = null;  
	     BASE64Decoder decoder = new BASE64Decoder();
	     try {  

	    	 bt = decoder.decodeBuffer(iStrBase64);  
	     } catch (IOException e) {  
	    	 e.printStackTrace();  
	     }  
	      
	     return bt;  
	}
	 
	/**
	 * 將字串轉為Base64
	 * @param iData : 資料
	 * @return String : 結果
	 */
	public static String encodeBase64(String iData) {
		String base64Msg = "";
		BASE64Encoder encode = new BASE64Encoder();
		try {
			byte[] output = iData.getBytes("UTF-8");
						
			base64Msg = encode.encode(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return base64Msg;
	}
	
	/**
	 * 將Base64字串轉為實體檔案
	 * @param iStrBase64 : Base64字串
	 * @return byte[] : 結果
	 */
	public static void transBase64ToFile(String iFilePath , String iStrBase64String) {  
		try{
		    byte[] b = new byte[1024 * 512];// 每次傳送512k, 1024 = 1K
	        int readLen;
	        StringUtil stu = new StringUtil();
			byte[] fl = StringUtil.decodeBase64(iStrBase64String);
			
			InputStream inRes = new ByteArrayInputStream(fl);
			
			 // 產生實體檔案
	         FileOutputStream foutRes = new FileOutputStream(iFilePath);
	         while ((readLen = inRes.read(b, 0, b.length)) != -1) {
	        	 foutRes.write(b, 0, readLen);
	        	 foutRes.flush();
	             Runtime.getRuntime().gc();
	         }
	         inRes.close();
	         foutRes.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 取得檔案副檔名
	 */
	public static String getFileExtension(String i_filepath) {
		int intDotPos = i_filepath.lastIndexOf(".");
		return i_filepath.substring(intDotPos + 1);
	}
	
	/**
	 * 取得檔案檔名不包含副檔名
	 */
	public static String getFileNameNotExtension(String i_filepath) {
		int intDotPos = i_filepath.lastIndexOf(".");
		return i_filepath.substring(0, intDotPos);
	}

}