package com.fpg.ec.utility;

/**
 * 檢查JSP中資料的工具
 */
public class FormChecker {

	/**
	 * Constructor for FormChecker
	 */
	public FormChecker() {
		super();
	}

	public boolean checkMail(String i_str) {

		if (i_str.matches("^[_a-zA-Z0-9-]+([.][_a-zA-Z0-9-]+)*@[_a-zA-Z0-9-]+([.][_a-zA-Z0-9-]+)*$"))
			return true;
		else
			return false;
	}

	/**
	 * 檢查日期格式是否符合(YYYMMDD)
	 * 
	 * @param String
	 *            i_date : 日期字串
	 */
	public boolean checkChinDate1(String i_date) {
		i_date = i_date.trim();
		if (i_date.length() > 7 || i_date.length() < 6)
			return false;
		// check there are characters
		for (int i = 0; i < i_date.length(); i++) {
			char c = i_date.charAt(i);
			if (c <= 'z' && c >= 'A')
				return false;
		}
		String year = i_date.substring(0, i_date.length() - 4);
		String month = i_date.substring(i_date.length() - 4, i_date.length() - 2);
		String day = i_date.substring(i_date.length() - 2, i_date.length());

		int intYear = Integer.parseInt(year) + 1911;
		int intMonth = Integer.parseInt(month);
		int intDay = Integer.parseInt(day);
		if (intMonth > 12 || intMonth < 1)
			return false;

		int intMonthDays[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (intMonth == 2) { // check for 閏年
			if (intYear % 4 == 0) {
				if (intYear % 100 == 0 && intYear % 400 == 0 && (intDay > 29 || intDay < 1))
					return false;
				if (intYear % 100 == 0 && intYear % 400 != 0 && (intDay > intMonthDays[intMonth - 1] || intDay < 1))
					return false;
				if (intDay > 29 || intDay < 1)
					return false;
			} else {
				if (intDay > intMonthDays[intMonth - 1] || intDay < 1)
					return false;
			}
		} else {
			if (intDay > intMonthDays[intMonth - 1] || intDay < 1)
				return false;
		}

		return true;
	}

	/**
	 * 檢查日期時間格式是否符合 (YYYYMMDDhhmmss)
	 * 
	 * @param i_datetime
	 * @return
	 */
	public boolean checkDateTime(String i_datetime) {
		i_datetime = i_datetime.trim();
		if (i_datetime.length() != 14)
			return false;
		if (!checkDate(i_datetime.substring(0, 8)))
			return false;
		if (!checkTime(i_datetime.substring(8)))
			return false;

		return true;
	}

	/**
	 * 檢查日期格式是否符合(YYYYMMDD)
	 * 
	 * @param String
	 *            i_date : 日期字串
	 */
	public boolean checkDate(String i_date) {
		try {
			i_date = i_date.trim();
			if (i_date.length() != 8)
				return false;
			// check there are characters
			for (int i = 0; i < i_date.length(); i++) {
				char c = i_date.charAt(i);
				if (c <= 'z' && c >= 'A')
					return false;
			}
			String year = i_date.substring(0, i_date.length() - 4);
			String month = i_date.substring(i_date.length() - 4, i_date.length() - 2);
			String day = i_date.substring(i_date.length() - 2, i_date.length());

			int intYear = Integer.parseInt(year);
			int intMonth = Integer.parseInt(month);
			int intDay = Integer.parseInt(day);
			if (intMonth > 12 || intMonth < 1)
				return false;

			int intMonthDays[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
			if (intMonth == 2) { // check for 閏年
				if (intYear % 4 == 0) {
					if (intYear % 100 == 0 && intYear % 400 == 0 && (intDay > 29 || intDay < 1))
						return false;
					if (intYear % 100 == 0 && intYear % 400 != 0 && (intDay > intMonthDays[intMonth - 1] || intDay < 1))
						return false;
					if (intDay > 29 || intDay < 1)
						return false;
				} else {
					if (intDay > intMonthDays[intMonth - 1] || intDay < 1)
						return false;
				}
			} else {
				if (intDay > intMonthDays[intMonth - 1] || intDay < 1)
					return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 檢查日期時間格式是否符合(hhmmss)
	 * 
	 * @param String
	 *            i_dattm : 日期時間字串
	 */
	public boolean checkTime(String i_dattm) {
		try {
			i_dattm = i_dattm.trim();
			if (i_dattm.length() != 6)
				return false;
			// check there are characters
			for (int i = 0; i < i_dattm.length(); i++) {
				char c = i_dattm.charAt(i);
				if (c <= 'z' && c >= 'A')
					return false;
			}

			String hour = i_dattm.substring(i_dattm.length() - 6, i_dattm.length() - 4);
			String minute = i_dattm.substring(i_dattm.length() - 4, i_dattm.length() - 2);
			String second = i_dattm.substring(i_dattm.length() - 2, i_dattm.length());

			int intHour = Integer.parseInt(hour);
			int intMinute = Integer.parseInt(minute);
			int intSecond = Integer.parseInt(second);
			if (intHour > 23 || intHour < 0)
				return false;
			if (intMinute > 59 || intMinute < 0)
				return false;
			if (intSecond > 59 || intSecond < 0)
				return false;

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 檢查日期時間格式是否符合(YYYMMDDHHmm)
	 * 
	 * @param String
	 *            i_dattm : 日期時間字串
	 */
	public boolean checkChinDattm1(String i_dattm) {
		i_dattm = i_dattm.trim();
		if (i_dattm.length() > 11 || i_dattm.length() < 10)
			return false;
		// check there are characters
		for (int i = 0; i < i_dattm.length(); i++) {
			char c = i_dattm.charAt(i);
			if (c <= 'z' && c >= 'A')
				return false;
		}
		String year = i_dattm.substring(0, i_dattm.length() - 8);
		String month = i_dattm.substring(i_dattm.length() - 8, i_dattm.length() - 6);
		String day = i_dattm.substring(i_dattm.length() - 6, i_dattm.length() - 4);
		String hour = i_dattm.substring(i_dattm.length() - 4, i_dattm.length() - 2);
		String minute = i_dattm.substring(i_dattm.length() - 2, i_dattm.length());

		int intYear = Integer.parseInt(year) + 1911;
		int intMonth = Integer.parseInt(month);
		int intDay = Integer.parseInt(day);
		if (intMonth > 12 || intMonth < 1)
			return false;

		int intMonthDays[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (intMonth == 2) { // check for 閏年
			if (intYear % 4 == 0) {
				if (intYear % 100 == 0 && intYear % 400 == 0 && (intDay > 29 || intDay < 1))
					return false;
				if (intYear % 100 == 0 && intYear % 400 != 0 && (intDay > intMonthDays[intMonth - 1] || intDay < 1))
					return false;
				if (intDay > 29 || intDay < 1)
					return false;
			} else {
				if (intDay > intMonthDays[intMonth - 1] || intDay < 1)
					return false;
			}
		} else {
			if (intDay > intMonthDays[intMonth - 1] || intDay < 1)
				return false;
		}

		int intHour = Integer.parseInt(hour);
		int intMinute = Integer.parseInt(minute);
		if (intHour > 23 || intHour < 0)
			return false;
		if (intMinute > 59 || intMinute < 0)
			return false;

		return true;
	}

	/**
	 * 檢查日期時間格式是否符合(YYYYMMDDHHmm)
	 * 
	 * @param String
	 *            i_dattm : 日期時間字串
	 */
	public boolean checkDattm1(String i_dattm) {
		i_dattm = i_dattm.trim();
		if (i_dattm.length() != 12)
			return false;
		// check there are characters
		for (int i = 0; i < i_dattm.length(); i++) {
			char c = i_dattm.charAt(i);
			if (c <= 'z' && c >= 'A')
				return false;
		}
		String year = i_dattm.substring(0, i_dattm.length() - 8);
		String month = i_dattm.substring(i_dattm.length() - 8, i_dattm.length() - 6);
		String day = i_dattm.substring(i_dattm.length() - 6, i_dattm.length() - 4);
		String hour = i_dattm.substring(i_dattm.length() - 4, i_dattm.length() - 2);
		String minute = i_dattm.substring(i_dattm.length() - 2, i_dattm.length());

		int intYear = Integer.parseInt(year);
		int intMonth = Integer.parseInt(month);
		int intDay = Integer.parseInt(day);
		if (intMonth > 12 || intMonth < 1)
			return false;

		int intMonthDays[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (intMonth == 2) { // check for 閏年
			if (intYear % 4 == 0) {
				if (intYear % 100 == 0 && intYear % 400 == 0 && (intDay > 29 || intDay < 1))
					return false;
				if (intYear % 100 == 0 && intYear % 400 != 0 && (intDay > intMonthDays[intMonth - 1] || intDay < 1))
					return false;
				if (intDay > 29 || intDay < 1)
					return false;
			} else {
				if (intDay > intMonthDays[intMonth - 1] || intDay < 1)
					return false;
			}
		} else {
			if (intDay > intMonthDays[intMonth - 1] || intDay < 1)
				return false;
		}

		int intHour = Integer.parseInt(hour);
		int intMinute = Integer.parseInt(minute);
		if (intHour > 23 || intHour < 0)
			return false;
		if (intMinute > 59 || intMinute < 0)
			return false;

		return true;
	}

	/**
	 * 檢查資料長度是否超出限制
	 * 
	 * @param String
	 *            i_data : 資料值
	 * @param int i_maxlength : 長度限制
	 */
	public boolean checkMaxLength(String i_data, int i_maxlength) {
		if (i_data.length() == 0)
			return true;

		int intRealLength = 0;
		intRealLength = countDataLength(i_data);

		if (intRealLength > i_maxlength)
			return false;
		return true;
	}

	/**
	 * 檢查資料長度等於限制之長度
	 * 
	 * @param String
	 *            i_data : 資料值
	 * @param int i_length : 長度限制
	 */
	public boolean checkLength(String i_data, int i_length) {

		int intRealLength = 0;
		intRealLength = countDataLength(i_data);

		if (intRealLength != i_length)
			return false;
		return true;
	}

	/**
	 * 檢查資料是否為數字
	 * 
	 * @param String
	 *            i_data : 資料值
	 */
	public boolean checkDigit(String i_data) {
		for (int i = 0; i < i_data.length(); i++) {
			char c = i_data.charAt(i);
			if (c < '0' || c > '9')
				return false;
		}
		return true;
	}

	/**
	 * 檢查資料是否皆為英文字母
	 * 
	 * @param String
	 *            i_data : 資料值
	 */
	public boolean checkCharacter(String i_data) {
		i_data = i_data.toLowerCase();
		for (int i = 0; i < i_data.length(); i++) {
			char c = i_data.charAt(i);
			if (c < 'a' || c > 'z')
				return false;
		}
		return true;
	}

	/**
	 * 檢查資料是否為數值
	 * 
	 * @param String
	 *            i_data : 資料值
	 */
	public boolean checkNum(String i_data) {
		try {
			new java.math.BigDecimal(i_data);
		} catch (java.lang.NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 檢查資料數值長度是否在設定範圍內
	 * 
	 * @param String
	 *            i_data : 資料值
	 * @param int i_intnum : 整數位數長度
	 * @param int i_decimalnum : 小數位數長度
	 */
	public boolean checkNumMaxLength(String i_data, int i_intnum, int i_decimalnum) {
		try {
			new java.math.BigDecimal(i_data);
		} catch (java.lang.NumberFormatException e) {
			return false;
		}

		int intPointPos = i_data.indexOf(".");
		int intIntegerLen = 0;
		int intDecimalLen = 0;
		if (intPointPos > 0) {
			intIntegerLen = intPointPos;
			intDecimalLen = i_data.length() - intPointPos - 1;
			if (intIntegerLen > i_intnum)
				return false;
			if (intDecimalLen > i_decimalnum)
				return false;
		} else {
			intIntegerLen = i_data.length();
			if (intIntegerLen > i_intnum)
				return false;
		}
		return true;
	}

	/**
	 * 計算資料長度
	 * 
	 * @param i_data
	 *            : 資料值
	 * @return int : 字串長度
	 */
	public int countDataLength(String i_data) {
		int intReturn = 0;
		for (int i = 0; i < i_data.length(); i++) {
			int c = i_data.charAt(i);
			if (Integer.toHexString(c).length() > 2)
				intReturn += 3; // 全形字
			else if (c > 127)
				intReturn += 3; // >127
			else
				intReturn += 1;

			// System.out.println(i_data.charAt(i)+":"+c);
		}
		return intReturn;
	}

	/**
	 * 計算資料長度
	 * 
	 * @param i_data
	 *            : 資料值
	 * @return int : 字串長度
	 */
	public int countDataLengthByUTF8Byte(String i_data) {
		int intReturn = 0;
		try {
			intReturn = i_data.getBytes("utf8").length;
		} catch (Exception e) {
			e.printStackTrace();
			intReturn = -1;
		}
		return intReturn;
	}

	/**
	 * 檢查日期時間格式是否符合(YYYYMMDDHHmmss)
	 * 
	 * @param String
	 *            i_dattm : 日期時間字串
	 */
	public boolean checkDattm2(String i_dattm) {
		i_dattm = i_dattm.trim();
		if (i_dattm.length() != 14)
			return false;
		// check there are characters
		for (int i = 0; i < i_dattm.length(); i++) {
			char c = i_dattm.charAt(i);
			if (c <= 'z' && c >= 'A')
				return false;
		}
		String year = i_dattm.substring(0, i_dattm.length() - 10);
		String month = i_dattm.substring(i_dattm.length() - 10, i_dattm.length() - 8);
		String day = i_dattm.substring(i_dattm.length() - 8, i_dattm.length() - 6);
		String hour = i_dattm.substring(i_dattm.length() - 6, i_dattm.length() - 4);
		String minute = i_dattm.substring(i_dattm.length() - 4, i_dattm.length() - 2);
		String second = i_dattm.substring(i_dattm.length() - 2, i_dattm.length());

		int intYear = Integer.parseInt(year);
		int intMonth = Integer.parseInt(month);
		int intDay = Integer.parseInt(day);
		if (intMonth > 12 || intMonth < 1)
			return false;

		int intMonthDays[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (intMonth == 2) { // check for 閏年
			if (intYear % 4 == 0) {
				if (intYear % 100 == 0 && intYear % 400 == 0 && (intDay > 29 || intDay < 1))
					return false;
				if (intYear % 100 == 0 && intYear % 400 != 0 && (intDay > intMonthDays[intMonth - 1] || intDay < 1))
					return false;
				if (intDay > 29 || intDay < 1)
					return false;
			} else {
				if (intDay > intMonthDays[intMonth - 1] || intDay < 1)
					return false;
			}
		} else {
			if (intDay > intMonthDays[intMonth - 1] || intDay < 1)
				return false;
		}

		int intHour = Integer.parseInt(hour);
		int intMinute = Integer.parseInt(minute);
		int intSec = Integer.parseInt(second);
		if (intHour > 23 || intHour < 0)
			return false;
		if (intMinute > 59 || intMinute < 0)
			return false;
		if (intSec > 59 || intSec < 0)
			return false;

		return true;
	}

	/**
	 * 檢查資料是否皆為英文及數字
	 * @param String i_data : 資料值
	 */
	public boolean checkCharDigit(String i_data) {
		i_data = i_data.toLowerCase();
		for (int i = 0; i < i_data.length(); i++) {
			char c = i_data.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {

			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 檢查資料是否為大於零
	 * @param String i_data : 資料值
	 */
	public boolean checkNumGZero(String i_data) {
		try {
			java.math.BigDecimal bid0 = new java.math.BigDecimal(0);
			java.math.BigDecimal bidValue = new java.math.BigDecimal(i_data);

			if (bidValue.compareTo(bid0) <= 0) {
				return false;
			}
		} catch (java.lang.NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 檢查資料是否為大於等於零
	 * @param String i_data : 資料值
	 */
	public boolean checkNumGEZero(String i_data) {
		try {
			java.math.BigDecimal bid0 = new java.math.BigDecimal(0);
			java.math.BigDecimal bidValue = new java.math.BigDecimal(i_data);

			if (bidValue.compareTo(bid0) < 0) {
				return false;
			}
		} catch (java.lang.NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 檢查資料是否無全型字
	 * @param String i_data : 資料值
	 */
	public boolean checkNoFulltype(String i_data) {
		i_data = i_data.toLowerCase();
		for (int i = 0; i < i_data.length(); i++) {
			String strOneWord = i_data.substring(i, i + 1);
			try {
				byte[] c = strOneWord.getBytes("utf8");//中文字時 ==> length = 3
				if (c.length == 3) {
					return false;
				}
			} catch (Exception e) {

			}
		}
		return true;
	}
}
