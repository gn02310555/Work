package com.fpg.ec.utility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;

/** 
 * Big5 轉換為 Gb2312
 * @author pc18
 * @see Big2GbMapping, Big2GbPhraseMapping, Big2GbPhraseParser
 */
public class Big2GbConverter {

	/**
	 * Constructor for Big2GbConverter.
	 */
	public Big2GbConverter() {
		super();
	}

	/**
	 * 字元對字元轉換
	 * @param strBig5Source : Big5原始字串
	 * @return String : Gb字串
	 */
	public String convert(String strBig5Source) {
		StringBuffer strbGbResult = new StringBuffer();
		for (int i = 0; i < strBig5Source.length(); i++) {
			String strABig5Word =
				new Character(strBig5Source.charAt(i)).toString();
			if (strABig5Word.getBytes().length > 1) {
				strbGbResult.append(
					Big2GbMapping.getInstance().get(strABig5Word));
			} else {
				strbGbResult.append(strABig5Word);
			}
		}
		try {
			return new String(strbGbResult.toString().getBytes(), "GB2312");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 詞對詞轉換
	 * @param strBig5Source : Big5原始字串
	 * @return String : Gb字串
	 */
	public String convertWithPhrase(String strBig5Source) {
		Hashtable hstPhraseMapping = Big2GbPhraseMapping.getInstance();
		StringBuffer strbResult = new StringBuffer();
		int intMaxLen = 0;

		//取得Mapping Phrase之最大長度
		for (Enumeration enumTemp = hstPhraseMapping.keys();
		enumTemp.hasMoreElements();
			) {
			String strKey = (String) enumTemp.nextElement();
			int intLen = Integer.parseInt(strKey);
			if (intMaxLen < intLen) {
				intMaxLen = intLen;
			}
		}

		//比較並取代字串
		int intParsePos = 0;
		while (strBig5Source.length() - 1 >= intParsePos) {
			boolean isMatch = false;
			for (int intCompareLen = intMaxLen;
				intCompareLen >= 1;
				intCompareLen--) {
				try {
					String strCompare =
						strBig5Source.substring(
							intParsePos,
							intParsePos + intCompareLen);
					//System.out.println("c:"+strCompare+"("+intCompareLen+")");
					Hashtable hstTemp =
						(Hashtable) hstPhraseMapping.get(intCompareLen + "");
					if (hstTemp != null && hstTemp.containsKey(strCompare)) {
						strbResult.append((String) hstTemp.get(strCompare));
						intParsePos += intCompareLen;
						break;
					}
				} catch (IndexOutOfBoundsException e) {
					continue;
				}
			}
			if (strBig5Source.length() - 1 >= intParsePos && !isMatch) {
				strbResult.append(strBig5Source.charAt(intParsePos));
				intParsePos++;
			}
			//System.out.println(strbResult.toString());
		}

		//轉為gb碼
		return convert(strbResult.toString());
	}
	
	/**
	 * 對文檔作字元對字元轉換
	 * @param fileBig5Source : Big5原始字串
	 * @exception Exception : 轉檔失敗
	 */
	public void convert(java.io.File fileBig5Source) throws Exception {
		try {
			
			BufferedReader in =
				new BufferedReader(
					new InputStreamReader(new FileInputStream(fileBig5Source)));

			String strIn = null;
			String strNewLine = "\n";
			StringBuffer strbResult = new StringBuffer();
			while ((strIn = in.readLine()) != null) {
				strbResult.append(
					new Big2GbConverter().convert(strIn));
				strbResult.append(strNewLine);
			}
			in.close();
			
			FileOutputStream out = new FileOutputStream(fileBig5Source);
		    out.write(strbResult.toString().getBytes("GB2312"));
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 對文檔作詞對詞轉換
	 * @param fileBig5Source : Big5原始文檔
	 * @exception Exception : 轉檔失敗
	 */
	public void convertWithPhrase(java.io.File fileBig5Source) throws Exception {
		try {
			
			BufferedReader in =
				new BufferedReader(
					new InputStreamReader(new FileInputStream(fileBig5Source)));

			String strIn = null;
			String strNewLine = "\n";
			StringBuffer strbResult = new StringBuffer();
			while ((strIn = in.readLine()) != null) {
				strbResult.append(
					new Big2GbConverter().convertWithPhrase(strIn));
				strbResult.append(strNewLine);
			}
			in.close();
			
			FileOutputStream out = new FileOutputStream(fileBig5Source);
		    out.write(strbResult.toString().getBytes("GB2312"));
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	public static void main(String argc[]) throws Exception {
		//ring s = "郵遞區號奇";

		File fileObj = new File("d:/temp/b.txt");
		new Big2GbConverter().convertWithPhrase(fileObj);

		

	}

}
