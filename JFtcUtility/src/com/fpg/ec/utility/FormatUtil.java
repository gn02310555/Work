package com.fpg.ec.utility;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @date 2014-08-01
 * @author N000129263
 * @description 用於格式化相關系統文字
 */

public class FormatUtil {
	
	private static SimpleDateFormat dFormat;
	private static final int DefIntegerLength = 15;
	private static final int DefDecimalLength = 3;
	
	public static String getDateToFormat(String iDate) {
		return new StringUtil().toFormat(iDate, "####/##/##");
	}

	public static String getDateTimeToFormat(String iDateTime) {
		return new StringUtil().toFormat(iDateTime, "####/##/## ##:##:##");
	}

	
	
	public static boolean isDateValid(String iDate){
		try{
			dFormat = new SimpleDateFormat("yyyyMMdd");  
			dFormat.setLenient(false);
			Date d = dFormat.parse(iDate);                        
		}catch(ParseException e){
			//告訴user，這個日期不是一個正確的日期"
			return false;
		}
		return true;
	}
	
	public static boolean isDateTimeValid(String iDateTime){
		try{
			dFormat = new SimpleDateFormat("yyyyMMddHHmm");  
			dFormat.setLenient(false);
			Date d = dFormat.parse(iDateTime);                        
		}catch(ParseException e){
			//告訴user，這個日期不是一個正確的日期"
			return false;
		}
		return true;
	}
	
	/**
	 * 是否為數字
	 * @param IntegerLength
	 * @param DecimalLength
	 * @param strValue
	 * @return
	 */
	public static boolean isNumeric(int IntegerLength, int DecimalLength, String strValue) {
		boolean result = true;
		
		if(strValue.length() > 0) {
			IntegerLength = (IntegerLength == 0 ? DefIntegerLength : IntegerLength);
			DecimalLength = (DecimalLength == 0 ? DefDecimalLength : DecimalLength);
	
			String patternStr = "^-?\\d{0," + IntegerLength + "}+(\\.\\d{0,"+ DecimalLength + "})?";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(strValue);
	
			if (matcher.matches() == false) {
				result = false;
			}
		}
		else {
			result = false;
		}

		return result;
	}

	/**
	 * 是否為整數
	 * @param IntegerLength
	 * @param strValue
	 * @return
	 */
	public static boolean isInteger(int IntegerLength, String strValue) {
		boolean result = true;
		IntegerLength = (IntegerLength == 0 ? DefIntegerLength : IntegerLength);
		String patternStr = "^-?\\d{0," + IntegerLength + "}";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(strValue);

		if (matcher.matches() == false) {
			result = false;
		}

		return result;
	}
	
	/**
	 * 是否為英數字
	 * @param IntegerLength
	 * @param strValue
	 * @return
	 */
	public static boolean isEnglishOrNumber(String strValue) {
		boolean result = true;
		String patternStr = "^[A-Za-z0-9]+";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(strValue);

		if (matcher.matches() == false) {
			result = false;
		}

		return result;
	}
	
	/**
	 * 是否為郵件字串
	 * @param IntegerLength
	 * @param strValue
	 * @return
	 */
	public static boolean isEMail(String strValue) {
		boolean result = true;
		String patternStr = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(strValue);

		if (matcher.matches() == false) {
			result = false;
		}

		return result;
	}
	
	/**
	 * 位元數是否小於限制長度
	 * @param IntegerLength
	 * @param strValue
	 * @return
	 */
	public static boolean isLessThanMaxLength(int iMaxLength, String strValue) {
		boolean result = true;
		int iLength = strValue.length();

		if(iLength > iMaxLength) {
			result = false;
		}
		
		return result;
	}
	
	/**
	 * 位元數是否小於限制長度
	 * @param IntegerLength
	 * @param strValue
	 * @return
	 */
	public static boolean isGetByteLessThanMaxLength(int iMaxLength, String strValue) {
		boolean result = true;
		int iLength = strValue.getBytes().length;

		if(iLength > iMaxLength) {
			result = false;
		}
		
		return result;
	}
	
	/**
	 * 是否為台灣統一編號
	 * @param strValue
	 * @return
	 */
	public static boolean isTWNRFNo(String strValue) {
		boolean result = false;
		if(strValue.length() == 8 && isNumeric(8, 0, strValue)) {
			int iCheckMultipliers[] = {1,2,1,2,1,2,4,1};
			int iSumAll = 0;
			for(int i = 0; i < strValue.length(); i++) {
				String chrTaxNo = strValue.substring(i, i+1);
				int iTaxNo = Integer.valueOf(chrTaxNo);
				int iResult = iTaxNo * iCheckMultipliers[i];
				if(iResult > 9) {
					iResult = (iResult / 10) + (iResult % 10);
				}
				iSumAll += iResult;
			}
			
			if(iSumAll % 10 == 0) {
				result = true;
			} else if(String.valueOf(strValue.substring(6, 7)).equals("7") && (iSumAll + 1) % 10 == 0) {
				result = true;
			}
		}
		
		return result;
	}
	
	/**
	 * 是否為台灣身份證號
	 * @param strValue
	 * @return
	 */
	public static boolean isTWNPersonID(String strValue) {
		boolean result = false;
		 int n [] = new int[] {10,11,12,13,14,15,16,17,34,
                 18,19,20,21,22,35,23,24,25,26,27,28,29,32,30,31,33};
         char d [] = new char[] {'0','1','2','3','4',
                 '5','6','7','8','9'};
         char a [] = new char[] {'A','B','C','D','E','F',
                 'G','H','I','J','K','L','M','N','O','P',
                 'Q','R','S','T','U','V','W','X','Y','Z'};
     
		if(strValue.length() == 10) {
            char [] t = strValue.toCharArray();
            int test [] = new int [11];
            for(int s = 0; s < 26; s++){
                if(t[0] == a[s]){
                    int r = n[s];
                    test[0] = r / 10;
                    test[1] = r - test[0] * 10;
                }
            }
            for(int s = 1; s < 10; s++){
                for(int x = 0; x < 10; x++){
                    if(t[s] == d[x]){
                        test[s+1] = x;
                    }
                }
            }
            int j = test[0] + test[10];
            for(int k = 1; k < 10; k++)
                j = j + test[k] * (10-k);       
            if((j % 10) == 0){
            	result = true;
            }
            j = 0;
		}
		
		return result;
	}
	
	/**
	 * 檔案大小單位換算
	 * @author Aircon
	 * @reference    http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
	 */
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
}
