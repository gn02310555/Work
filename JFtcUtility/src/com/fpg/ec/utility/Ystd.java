package com.fpg.ec.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <PRE><CODE>
 * 處理日期的程式集
 *  </CODE></PRE>
 */
public class Ystd {

	public static String swt_err = " ";
	public static int xcnt = 0;
	private static SimpleDateFormat utime1Format = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:SSS");
	private static SimpleDateFormat utime4Format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static SimpleDateFormat utime5Format = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 日期加減幾日。
	 * @return java.lang.String
	 * @param xdate java.lang.String
	 * @param xdays int
	 * @exception java.io.IOException
	 */
	public String add_days(String xdate, int xdays) {
		try {
			String timeZoneID[] = TimeZone.getAvailableIDs();
			TimeZone timeZone;

			TimeZone tz = TimeZone.getDefault();
			Locale lc = Locale.getDefault();
			Calendar calendar;
			GregorianCalendar gc;
			int year, month, day;
			String fdate, mm, dd;
			//    gc = GregorianCalendar.getInstance(tz, lc);
			gc = new GregorianCalendar();
			swt_err = chk_date(xdate);
			if (swt_err == "*") {
				System.out.println("xdate error!!");
				return " ";
			}
			year = Integer.parseInt(xdate.substring(0, 4).trim());
			month = Integer.parseInt(xdate.substring(4, 6)) - 1;
			day = Integer.parseInt(xdate.substring(6, 8));
			gc.set(year, month, day, 0, 0, 0);
			//    gc.roll(GregorianCalendar.DAY,true);
			gc.add(GregorianCalendar.DAY_OF_MONTH, xdays);
			year = gc.get(GregorianCalendar.YEAR);
			month = gc.get(GregorianCalendar.MONTH) + 1;
			day = gc.get(GregorianCalendar.DAY_OF_MONTH);
			if (month < 10)
				mm = "0" + Integer.toString(month);
			else
				mm = Integer.toString(month);
			if (day < 10)
				dd = "0" + Integer.toString(day);
			else
				dd = Integer.toString(day);
			xdate = year + mm + dd;
			return xdate;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 日期(年月日)加減幾個月，傳回值不清除左邊空白。
	 * @return java.lang.String
	 * @param xdate java.lang.String
	 * @param xmons int
	 * @exception java.io.IOException
	 */
	public String add_mons(String xdate, int xmons) {
		try {

			String timeZoneID[] = TimeZone.getAvailableIDs();
			TimeZone timeZone;
			//    Locale locale=new Locale("zh","TW","Win");
			TimeZone tz = TimeZone.getDefault();
			Locale lc = Locale.getDefault();

			Calendar calendar;
			GregorianCalendar gc;
			int year, month, day;
			String fdate, yy, mm, dd;

			// Create Gregorian Calendar	
			gc = new GregorianCalendar();

			// Parse input date to year,month,day
			year = Integer.parseInt(xdate.substring(0, 4).trim());
			month = Integer.parseInt(xdate.substring(4, 6)) - 1;
			day = Integer.parseInt(xdate.substring(6, 8));

			// Add or Sub month
			gc.set(year, month, day, 0, 0, 0);
			gc.add(GregorianCalendar.MONTH, xmons);

			// Get year,month,day from Gregorian Calendar	
			year = gc.get(GregorianCalendar.YEAR);
			month = gc.get(GregorianCalendar.MONTH) + 1;
			day = gc.get(GregorianCalendar.DAY_OF_MONTH);

			// Format month,day
			yy = Integer.toString(year);
			for (int i = yy.length() + 1; i <= 4; i++) {
				yy = " " + yy;
			}
			if (month < 10)
				mm = "0" + Integer.toString(month);
			else
				mm = Integer.toString(month);
			if (day < 10)
				dd = "0" + Integer.toString(day);
			else
				dd = Integer.toString(day);

			fdate = yy + mm + dd;

			return fdate;

		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 日期(年月)加減幾個月，傳回值不清除左邊空白。
	 * @return java.lang.String
	 * @param xdate java.lang.String
	 * @param xmons int
	 * @exception java.io.IOException
	 */
	public String add_mons2(String xdate, int xmons) {
		try {

			String timeZoneID[] = TimeZone.getAvailableIDs();
			TimeZone timeZone;
			//    Locale locale=new Locale("zh","TW","Win");
			TimeZone tz = TimeZone.getDefault();
			Locale lc = Locale.getDefault();

			Calendar calendar;
			GregorianCalendar gc;
			int len = 0;
			int spacenum = 0;
			int year, month, day;
			String fdate, yy, mm, dd;

			// Create Gregorian Calendar	
			gc = new GregorianCalendar();

			// Parse input date to year,month,day
			len = xdate.length();
			month = Integer.parseInt(xdate.substring(len - 2)) - 1;
			year = Integer.parseInt(xdate.substring(0, len - 2).trim());
			day = 1;

			// Add or Sub month
			gc.set(year, month, day, 0, 0, 0);
			gc.add(GregorianCalendar.MONTH, xmons);

			// Get year,month,day from Gregorian Calendar	
			year = gc.get(GregorianCalendar.YEAR);
			month = gc.get(GregorianCalendar.MONTH) + 1;

			// Format month,day
			yy = Integer.toString(year);
			for (int i = yy.length() + 1; i <= len - 2; i++) {
				yy = " " + yy;
			}
			mm = Integer.toString(month);
			for (int i = mm.length() + 1; i <= 2; i++) {
				mm = "0" + mm;
			}

			fdate = yy + mm;

			return fdate;

		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 計算兩個日期間差異天數。
	 * @return	int
	 * @param	xdate1	java.lang.String
	 * @param	xdate2	java.lang.String
	 * @exception java.io.IOException
	 */
	public int bt_date(String xdate1, String xdate2) {
		try {
			String timeZoneID[] = TimeZone.getAvailableIDs();
			TimeZone timeZone;
			//    Locale locale=new Locale("zh","TW","Win");
			TimeZone tz = TimeZone.getDefault();
			Locale lc = Locale.getDefault();
			Calendar calendar;
			Date d1, d2;
			GregorianCalendar gc1, gc2;
			int year, month, day, xdays;
			String fdate, mm, dd;

			gc1 = new GregorianCalendar();
			gc2 = new GregorianCalendar();
			swt_err = chk_date(xdate1);
			if (swt_err == "*") {
				System.out.println("xdate1 error!!");
				return 0;
			}
			swt_err = chk_date(xdate2);
			if (swt_err == "*") {
				System.out.println("xdate2 error!!");
				return 0;
			}
			year = Integer.parseInt(xdate1.substring(0, 4));
			month = Integer.parseInt(xdate1.substring(4, 6)) - 1;
			day = Integer.parseInt(xdate1.substring(6, 8));
			gc1.set(year, month, day, 0, 0, 0);
			year = Integer.parseInt(xdate2.substring(0, 4));
			month = Integer.parseInt(xdate2.substring(4, 6)) - 1;
			day = Integer.parseInt(xdate2.substring(6, 8));
			gc2.set(year, month, day, 0, 0, 0);
			d1 = gc1.getTime();
			d2 = gc2.getTime();
			return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return 0;
		}
	}

	/**
	 * 計算兩個時間(指udate2方法所傳回的毫秒時間)之差時,以秒為單位。
	 * @param	ldate1	String
	 * @param	ldate2	String
	 * @return	String
	 * @exception java.io.IOException
	 */
	public String bt_date2(String ldate1, String ldate2) {
		try {
			long diff_time = Long.parseLong(ldate2, 10) - Long.parseLong(ldate1, 10);
			float ftime = (float) diff_time / 1000;
			String stime = String.valueOf(ftime);
			return stime;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 日期檢查。
	 * @return java.lang.String
	 * @param xdate int
	 * @exception java.io.IOException
	 */
	public String chk_date(int xdate) {
		try {
			String wrk_date = Integer.toString(xdate);
			int len = wrk_date.length();
			if (len == 5)
				wrk_date = "   " + wrk_date;
			else if (len == 6)
				wrk_date = "  " + wrk_date;
			else if (len == 7)
				wrk_date = " " + wrk_date;
			
			return chk_date(wrk_date);
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 日期檢查。
	 * @return java.lang.String
	 * @param xdate java.lang.String
	 * @exception java.io.IOException
	 */
	public String chk_date(String xdate) {
		try {

			GregorianCalendar gc;
			int year, month, day, jmonth;
			int lmaxday;

			day = Integer.parseInt(xdate.substring(xdate.length() - 2, xdate.length()));
			month = Integer.parseInt(xdate.substring(xdate.length() - 4, xdate.length() - 2));
			year = Integer.parseInt(xdate.substring(0, xdate.length() - 4).trim());

			jmonth = month - 1;

			//	  System.out.println(year);

			gc = new GregorianCalendar();

			boolean isleap = gc.isLeapYear(year);

			gc.set(year, jmonth, day, 0, 0, 0);

			if (year < 0)
				return "*";

			if (month >= 1 && month <= 12) {
				if (month == 2) {
					if (isleap) {
						if (day > 0 && day <= 29)
							return " ";
						else
							return "*";
					} else {
						if (day > 0 && day <= 28)
							return " ";
						else
							return "*";
					}
				}
				if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
					if (day > 0 && day <= 30)
						return " ";
					else
						return "*";
				} else if ((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10) || (month == 12)) {
					if (day > 0 && day <= 31)
						return " ";
					else
						return "*";
				}
			}
			return "*";
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 日期檢查。
	 * @return java.lang.String
	 * @param xdate java.lang.String
	 * @exception java.io.IOException
	 */
	public String chk_date(String xdate, int cnt) {
		try {
			String rtcode = chk_date(xdate);

			if (rtcode.equals("*")) {
				return "*";
			} else {
				String xdate1 = xdate.trim();
				for (int i = xdate1.length() + 1; i <= cnt; i++) {
					xdate1 = " " + xdate1;
				}
				return xdate1;
			}

		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 顯示某日期之月底日。
	 * @return int
	 * @param xdate java.lang.int
	 * @exception java.io.IOException
	 */
	public int eom_date(int xdate) {
		try {
			String wrk_date, fdate;
			int mdate;
			String odate = new String();
			fdate = Integer.toString(xdate);
			swt_err = chk_date(fdate);
			if (swt_err == "*") {
				System.out.println("xdate error!!");
				return 0;
			}
			wrk_date = fdate.substring(0, 6);
			for (int ix = 28; ix <= 31; ix++) {
				wrk_date = wrk_date.substring(0, 6) + Integer.toString(ix);
				swt_err = chk_date(wrk_date);
				if (swt_err == "*") {
					swt_err = " ";
					ix = 32;
				} else
					odate = wrk_date;
			}
			mdate = Integer.parseInt(odate);
			return mdate;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return 0;
		}
	}

	/**
	 * 顯示某日期之月底日。
	 * @return java.lang.String
	 * @param xdate java.lang.String
	 * @exception java.io.IOException
	 */
	public String eom_date(String xdate) {
		try {
			String wrk_date;
			String odate = new String();
			swt_err = chk_date(xdate);
			if (swt_err == "*") {
				System.out.println("xdate error!!");
				return " ";
			}
			wrk_date = xdate.substring(0, 6);
			for (int ix = 28; ix <= 31; ix++) {
				wrk_date = wrk_date.substring(0, 6) + Integer.toString(ix);
				swt_err = chk_date(wrk_date);
				if (swt_err == "*") {
					swt_err = " ";
					ix = 32;
				} else
					odate = wrk_date;
			}
			return odate;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 處理Excption。
	 * @return java.lang.String
	 * @param except java.lang.Exception
	 */
	public String hdl_err(Exception except) {
		try {
			throw except;
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("CaughtStringIndexOutOfBoundsException: " + e.getMessage());
			return "*";
		} catch (NumberFormatException e) {
			System.err.println("CaughtNumberFormatException: " + e.getMessage());
			return "*";
		} catch (IllegalArgumentException e) {
			System.err.println("CaughtIllegalArgumentException: " + e.getMessage());
			return "*";
		} catch (Exception e) {
			System.err.println("CaughtException: " + e.getMessage());
			return "*";
		}
	}

	/**
	 * 國曆轉西曆。
	 * @return int
	 * @param xdate int (例如:890720)
	 * @exception java.io.IOException
	 */
	public int tr_date(int xdate) {
		try {
			// 轉為西元年進行格式檢核
			int fdate = xdate + 19110000;
			swt_err = chk_date(fdate);
			//	System.out.println(swt_err);
			if (swt_err == "*") {
				System.out.println("xdate(" + xdate  + ") error!!");
				return 0;
			}

			return fdate;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return 0;
		}
	}

	/**
	 * 國曆轉西曆。
	 * @return java.lang.String
	 * @param xdate java.lang.String (例如："  870721")
	 * @exception java.io.IOException
	 */
	public String tr_date(String xdate) {
		try {
			int fdate;
			String xdate2 = ""; // formatting xdate

			// 去除xdate的"/"
			xdate2 = xdate.replaceAll("/", "").trim();
			// 轉為西元年進行格式檢核
			fdate = Integer.parseInt(xdate2) + 19110000;
			swt_err = chk_date(fdate);
			//	System.out.println(swt_err);
			if (swt_err == "*") {
				System.out.println("xdate(" + xdate + ") error!!");
				return " ";
			}

			return Integer.toString(fdate);
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 西曆轉國曆。
	 * @return int
	 * @param xdate int (例如：20000620)
	 * @exception java.io.IOException
	 */
	public int tr_roc(int xdate) {
		try {
			String swt_err = chk_date(xdate);
			if (swt_err == "*") {
				System.out.println("xdate error!!");
				return 0;
			}
			int tr_date = xdate - 19110000;
			return tr_date;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			swt_err = "*";
			return 0;
		}
	}

	/**
	 * 西曆轉國曆。
	 * @return java.lang.String
	 * @param xdate java.lang.String (例如："20000620"或"2000/06/20")
	 * @exception java.io.IOException
	 */
	public String tr_roc(String xdate) {
		

	    StringUtil su = new StringUtil();
	    xdate = xdate.replaceAll("/", "");
	    xdate = su.lpad(xdate.trim(),8,'0');
	    
	    String strYear = xdate.substring(0,4);
	    int intYear = Integer.parseInt(strYear);
	    intYear = intYear - 1911;
	    
	    String strReturn = intYear+xdate.substring(4);
	    strReturn = su.lpad(strReturn,8,' ');
	    
	    return strReturn;

		/*
		try {
			String wrk_date;
			String xdate2 = ""; // formatting xdate

			// 去除xdate的"/"
			for (int i = 0; i < xdate.length(); i++) {
				if (xdate.charAt(i) != '/') {
					xdate2 = xdate2 + xdate.charAt(i);
				}
			}

			wrk_date = add_mons(xdate2, -22932);

			int len = wrk_date.length();
			if (len == 6)
				wrk_date = "  " + wrk_date;
			if (len == 7)
				wrk_date = "  " + wrk_date;

			swt_err = " ";
			return wrk_date;

		} catch (Exception e) {
			String swt_err = hdl_err(e);
			swt_err = "*";
			return " ";
		}
		*/
	}

	/**
	 * YYMM日期檢查。
	 * @return java.lang.String
	 * @param xyymm java.lang.String
	 * @exception java.io.IOException
	 */
	public String tr_yymm(String xyymm) {
		try {

			String xdate = xyymm + "01";
			swt_err = chk_date(xdate);
			if (swt_err == "*")
				return "*";
			else
				return " ";

		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * YYMM日期檢查。
	 * @return java.lang.String
	 * @param xyymm java.lang.String
	 * @exception java.io.IOException
	 */
	public String tr_yymm(String xyymm, int cnt) {
		try {

			String xdate = xyymm + "01";
			String rt = chk_date(xdate, cnt + 2);

			if (rt.equals("*")) {
				return "*";
			} else {
				return rt.substring(0, cnt);
			}

		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 擷取目前系統日期(YYYYMMDD)。
	 * @return java.lang.String (例如："20000731")
	 * @exception java.io.IOException
	 */
	public String udate() {
		try {
			String timeZoneID[] = TimeZone.getAvailableIDs();
			TimeZone timeZone;
			//    Locale locale=new Locale("zh","TW","Win");
			TimeZone tz = TimeZone.getDefault();
			Locale lc = Locale.getDefault();
			Calendar calendar;
			int year, month, day, hour, minute, second;
			String fdate, mm, dd;

			calendar = Calendar.getInstance(tz, lc);

			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
			day = calendar.get(Calendar.DAY_OF_MONTH);
			if (month < 10)
				mm = "0" + Integer.toString(month);
			else
				mm = Integer.toString(month);
			if (day < 10)
				dd = "0" + Integer.toString(day);
			else
				dd = Integer.toString(day);
			second = calendar.get(Calendar.SECOND);
			minute = calendar.get(Calendar.MINUTE);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			fdate = Integer.toString(year) + mm + dd;
			return fdate;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 擷取系統目前時間與1970年1月1日UTC間的差距,以毫秒為單位。
	 * @return java.lang.String
	 * @exception java.io.IOException
	 */

	public String udate2() {
		try {
			long time = System.currentTimeMillis();
			return Long.toString(time);
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	public static String udate(int offset) {
		Calendar C = Calendar.getInstance();
		C.add(Calendar.HOUR, offset);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(C.getTime());
	}

	public static String udattm(int offset) {
		Calendar C = Calendar.getInstance();
		C.add(Calendar.HOUR, offset);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(C.getTime());
	}

	/**
	 * 擷取目前系統日期時間(YYYYMMDDHH24MISS)。
	 * @return java.lang.String (例如："20000731173020")
	 * @exception java.io.IOException
	 */
	public String utime() {
		try {
			String timeZoneID[] = TimeZone.getAvailableIDs();
			TimeZone timeZone;
			//    Locale locale=new Locale("zh","TW","Win");
			TimeZone tz = TimeZone.getDefault();
			Locale lc = Locale.getDefault();
			Calendar calendar;
			int year, month, day, hour, minute, second;
			String ftime, hh, mi, ss;

			calendar = Calendar.getInstance(tz, lc);

			second = calendar.get(Calendar.SECOND);
			minute = calendar.get(Calendar.MINUTE);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			if (hour < 10)
				hh = "0" + Integer.toString(hour);
			else
				hh = Integer.toString(hour);
			if (minute < 10)
				mi = "0" + Integer.toString(minute);
			else
				mi = Integer.toString(minute);
			if (second < 10)
				ss = "0" + Integer.toString(second);
			else
				ss = Integer.toString(second);
			ftime = udate() + hh + mi + ss;
			return ftime;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 擷取目前系統日期時間(YYYYMMDDHH24MISS)+增加或減少秒數。
	 * @return java.lang.String (例如："20000731173020" + offset(20) = "20000731173040")
	 * @exception java.io.IOException
	 */
	public String utimeOffsetSec(int offset) {
		try {
			String timeZoneID[] = TimeZone.getAvailableIDs();
			TimeZone timeZone;
			//    Locale locale=new Locale("zh","TW","Win");
			TimeZone tz = TimeZone.getDefault();
			Locale lc = Locale.getDefault();
			Calendar calendar;
			int year, month, day, hour, minute, second;
			String ftime, hh, mi, ss;

			calendar = Calendar.getInstance(tz, lc);

			calendar.add(Calendar.SECOND, offset);
			second = calendar.get(Calendar.SECOND);
			minute = calendar.get(Calendar.MINUTE);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			if (hour < 10)
				hh = "0" + Integer.toString(hour);
			else
				hh = Integer.toString(hour);
			if (minute < 10)
				mi = "0" + Integer.toString(minute);
			else
				mi = Integer.toString(minute);
			if (second < 10)
				ss = "0" + Integer.toString(second);
			else
				ss = Integer.toString(second);
			ftime = udate() + hh + mi + ss;
			return ftime;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}
	
	/**
	 * 擷取目前系統日期時間(YYYYMMDDHH24MISSsss)。
	 * @return java.lang.String (例如："2001-10-09-16-27-05-660")
	 * @exception java.io.IOException
	 */
	public static String utime1() {
		return utime1Format.format(new Date());
	}

	/**
	 * 擷取目前系統時分杪(HH24MISS)。
	 * @return java.lang.String (例如："173020")
	 * @exception java.io.IOException
	 */
	public String utime2() {
		try {
			String timeZoneID[] = TimeZone.getAvailableIDs();
			TimeZone timeZone;
			//    Locale locale=new Locale("zh","TW","Win");
			TimeZone tz = TimeZone.getDefault();
			Locale lc = Locale.getDefault();
			Calendar calendar;
			int year, month, day, hour, minute, second;
			String ftime, hh, mi, ss;

			calendar = Calendar.getInstance(tz, lc);

			second = calendar.get(Calendar.SECOND);
			minute = calendar.get(Calendar.MINUTE);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			if (hour < 10)
				hh = "0" + Integer.toString(hour);
			else
				hh = Integer.toString(hour);
			if (minute < 10)
				mi = "0" + Integer.toString(minute);
			else
				mi = Integer.toString(minute);
			if (second < 10)
				ss = "0" + Integer.toString(second);
			else
				ss = Integer.toString(second);
			ftime = hh + mi + ss;
			return ftime;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 擷取目前系統日期時間(國歷)(YYYMMDDHH24MI)。
	 * @return java.lang.String (例如：" 9103110730")
	 * @exception java.io.IOException
	 */
	public String utime3() {
		String strReturn = "";
		String now = utime();
		String year = now.substring(0, 4);
		int intYearNum = Integer.parseInt(year);
		intYearNum -= 1911; //轉為國曆年
		strReturn = intYearNum + now.substring(4, now.length() - 2);

		//若未至百年則開頭補空白
		if (strReturn.length() == 10)
			strReturn = " " + strReturn;

		return strReturn;
	}

	/**
	 * 擷取目前系統日期時間(YYYYMMDDHH24MISSsss)。
	 */
	public static String utime4() {
		return utime4Format.format(new Date());
	}

	/**
	 * 擷取目前系統日期時間(YYYYMMDDHH24MISS)。
	 */
	public static String utime5() {
		return utime5Format.format(new Date());
	}

	/**
	 * 將含有","之字串，以","區分後個別置放於Array。
	 * @return java.lang.String[] (例如：array[0]="mary", array[1]="john", array[2]="joe")
	 * @param str java.lang.String (例如："mary,john,joe")
	 * @exception java.io.IOException
	 */
	public String[] ycmd(String str) {
		try {
			int ix, iy, xlen, st;
			String dot = ",";
			String xstr;
			String[] ary = new String[80];
			xstr = str.trim();
			xlen = xstr.length();
			iy = 0;
			st = 0;
			for (ix = 0; ix < xlen; ix++) {
				if (xstr.substring(ix, ix + 1).equals(dot)) {
					ary[iy] = xstr.substring(st, ix);
					iy = iy + 1;
					st = ix + 1;
				}
			}
			ary[iy] = xstr.substring(st, xlen);
			Ystd.xcnt = iy + 1;
			return ary;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return null;
		}
	}

	/**
	 * 將字串以某一特殊符號區分(如","或"."或";"等）後，個別置放於Array中。
	 * @return java.lang.String[]  (例如：array[0]="mary", array[1]="john", array[2]="joe")
	 * @param str java.lang.String (例如："mary;john;joe")
	 * @param xdot java.lang.String (例如：";")
	 * @exception java.io.IOException
	 */
	public String[] ycmd(String str, String xdot) {
		try {
			int ix, iy, xlen, st;
			String dot = xdot;
			String xstr;
			String[] ary = new String[80];
			xstr = str.trim();
			xlen = xstr.length();
			iy = 0;
			st = 0;
			for (ix = 0; ix < xlen; ix++) {
				if (xstr.substring(ix, ix + 1).equals(dot)) {
					ary[iy] = xstr.substring(st, ix);
					iy = iy + 1;
					st = ix + 1;
				}
			}
			ary[iy] = xstr.substring(st, xlen);
			Ystd.xcnt = iy + 1;
			return ary;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return null;
		}
	}

	/**
	 * 身份證號碼檢查。
	 * @return java.lang.String
	 * @param xid java.lang.String
	 * @exception java.lang.NumberFormatException
	 */
	public String yidchk(String xid) {
		try {
			String id = xid.toUpperCase();
			String str = "ABCDEFGHJKLMNPQRSTUVXYZIO";
			int index = str.indexOf(id.charAt(0)) + 10;
			int sum = 0;
			int len = id.length();
			int num1 = 0;
			int[] numlist = new int[len];
			if (len != 10)
				return "*";
			String str2 = Integer.toString(index);
			numlist[0] = Integer.parseInt(str2.substring(0, 1));
			numlist[1] = Integer.parseInt(str2.substring(1, 2));

			for (int i = 2; i < len; i++) {
				numlist[i] = Integer.parseInt(id.substring(i - 1, i));
				if ((numlist[i] < 0) || (numlist[i] > 9))
					return "*";
			}

			for (int j = 1; j < len; j++)
				sum = sum + numlist[j] * (10 - j);
			sum = sum + numlist[0];
			int checknum = Integer.parseInt(id.substring(len - 1, len));
			//			System.out.println(sum);
			//			System.out.println(checknum);

			if ((10 - sum % 10) == checknum)
				return " ";
			else
				return "*";
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * 將字申某些位置以另外字串覆蓋。
	 * @return java.lang.String (例如："12x456x89")
	 * @param str java.lang.String (例如："123456789")
	 * @param xpos java.lang.String (例如："3,7")
	 * @param xovl java.lang.String (例如："x")
	 * @exception java.io.IOException
	 */
	public String yovl(String str, String xpos, String xovl) {
		try {
			String[] ary_str = new String[80];
			String[] xary = new String[80];
			int ix, iy, iz, xlen, len_str, st, xcnt;
			String xstr, w_str;
			String dot = ",";
			len_str = str.length();
			//	System.out.println(str+len_str);
			for (iy = 0; iy < len_str; iy++) {
				ary_str[iy] = str.substring(iy, iy + 1);
				//		System.out.println(ary_str[iy]);
			}
			xary = ycmd(xpos);
			xcnt = Ystd.xcnt;
			for (ix = 0; ix < xcnt; ix++) {
				st = Integer.parseInt(xary[ix].trim());
				xlen = xovl.length();
				iz = 0;
				for (iy = st - 1; iy < st + xlen - 1; iy++) {
					ary_str[iy] = xovl.substring(iz, iz + 1);
					iz = iz + 1;
					if (iy > len_str)
						len_str = iy;
				}
			}
			str = ary_str[0];
			for (ix = 1; ix < len_str; ix++) {
				str = str + ary_str[ix];
			}
			return str;
		} catch (Exception e) {
			String swt_err = hdl_err(e);
			return "*";
		}
	}

	/**
	 * <PRE><CODE>
	 *  方法名稱︰取得中文日期格式
	 *  方法說明︰
	 *  範例︰輸入20010101 輸出 西元 2001 年 01 月 01 日 
	 *  @param
	 *  @return
	 *  @exception
	 *  @see also
	 *  @since
	 *  </CODE></PRE>
	 * @param i_date java.lang.String
	 */
	public String getDateC(String i_date) {
		if (i_date == null || i_date.length() == 0)
			return "";
		i_date = i_date.trim();
		int len = i_date.length();
		if (len > 8 || len < 6)
			return i_date;
		String year = i_date.substring(0, len - 4);
		String month = i_date.substring(len - 4, len - 2);
		String day = i_date.substring(len - 2, len);
		if (len == 8) {
			return "西元 " + year + " 年 " + month + " 月 " + day + " 日";
		} else {
			return "民國 " + year + " 年 " + month + " 月 " + day + " 日";
		}
	}

	/**
	 * <PRE><CODE>
	 *  方法名稱︰取得中文日期格式
	 *  方法說明︰
	 *  範例︰輸入20010101 輸出 西元 2001 年 01 月 01 日 
	 *  @param
	 *  @return
	 *  @exception
	 *  @see also
	 *  @since
	 *  </CODE></PRE>
	 * @param i_date java.lang.String
	 */
	public String getDateC(String i_date, boolean hasHeader) {
		if (i_date == null || i_date.length() == 0)
			return "";
		i_date = i_date.trim();
		int len = i_date.length();
		if (len > 8 || len < 6)
			return i_date;
		String year = i_date.substring(0, len - 4);
		String month = i_date.substring(len - 4, len - 2);
		String day = i_date.substring(len - 2, len);
		if (len == 8) {
			return ((hasHeader) ? "西元" : "") + year + "年" + month + "月" + day + "日";
		} else {
			return ((hasHeader) ? "民國" : "") + year + "年" + month + "月" + day + "日";
		}
	}

	/**
	 * <PRE><CODE>
	 *  方法名稱︰取得日期格式
	 *  範例︰輸入20010101 輸出01/01/2001
	 *  </CODE></PRE>
	 */
	public String getDateE(String i_date) {
		if (i_date == null || i_date.length() == 0)
			return null;
		int len = i_date.length();

		String year = i_date.substring(0, len - 4);
		String month = i_date.substring(len - 4, len - 2);
		String day = i_date.substring(len - 2, len);
		return month + "/" + day + "/" + year;
	}
	
	/**
	 * <PRE><CODE>
	 *  方法名稱︰取得日期格式
	 *  範例︰輸入20010101 輸出 2001/01/01
	 *  </CODE></PRE>
	 */	
	public String getDateE2(String i_date) {
		if (i_date == null || i_date.length() == 0)
			return null;
		int len = i_date.length();

		String year = i_date.substring(0, len - 4);
		String month = i_date.substring(len - 4, len - 2);
		String day = i_date.substring(len - 2, len);
		return year + "/" + month + "/" + day;
	}	

	/**
	 * <PRE><CODE>
	 *  方法名稱︰取得日期時間格式
	 *  方法說明︰
	 *  範例︰輸入20010101120000 輸出 2001/01/01  12:00:00
	 *  @param
	 *  @return
	 *  @exception
	 *  @see also
	 *  @since
	 *  </CODE></PRE>
	 * @param i_date java.lang.String
	 */
	public String getDateTimeC(String i_datetime) {

		if (i_datetime == null || i_datetime.length() == 0)
			return "";
		i_datetime = i_datetime.trim();
		int len = i_datetime.length();
		String year = i_datetime.substring(0, len - 10);
		String month = i_datetime.substring(len - 10, len - 8);
		String day = i_datetime.substring(len - 8, len - 6);
		String hh = i_datetime.substring(len - 6, len - 4);
		String mm = i_datetime.substring(len - 4, len - 2);
		String ss = i_datetime.substring(len - 2, len);
		return year + "年" + month + "月" + day + "日  " + hh + ":" + mm + ":" + ss;
	}

	/**
	 * <PRE><CODE>
	 *  方法名稱︰取得日期時間格式
	 *  方法說明︰
	 *  範例︰輸入20010101120000 輸出 01/01/2001  12:00:00
	 *  @param
	 *  @return
	 *  @exception
	 *  @see also
	 *  @since
	 *  </CODE></PRE>
	 * @param i_date java.lang.String
	 */
	public String getDateTimeE(String i_datetime) {

		if (i_datetime == null || i_datetime.length() == 0)
			return null;
		int len = i_datetime.length();
		String year = i_datetime.substring(0, len - 10);
		String month = i_datetime.substring(len - 10, len - 8);
		String day = i_datetime.substring(len - 8, len - 6);
		String hh = i_datetime.substring(len - 6, len - 4);
		String mm = i_datetime.substring(len - 4, len - 2);
		String ss = i_datetime.substring(len - 2, len);
		return month + "/" + day + "/" + year + "  " + hh + ":" + mm + ":" + ss;
	}

	/**
	 * <PRE><CODE>
	 *  方法名稱︰取得日期時間格式
	 *  方法說明︰
	 *  範例︰輸入20010101120000 輸出 2001/01/01  12:00:00
	 *  @param
	 *  @return
	 *  @exception
	 *  @see also
	 *  @since
	 *  </CODE></PRE>
	 * @param i_date java.lang.String
	 */
	public String getDateTimeE2(String i_datetime) {

		if (i_datetime == null || i_datetime.length() == 0)
			return null;
		int len = i_datetime.length();
		String year = i_datetime.substring(0, len - 10);
		String month = i_datetime.substring(len - 10, len - 8);
		String day = i_datetime.substring(len - 8, len - 6);
		String hh = i_datetime.substring(len - 6, len - 4);
		String mm = i_datetime.substring(len - 4, len - 2);
		String ss = i_datetime.substring(len - 2, len);
		return year + "/" + month + "/" + day + "  " + hh + ":" + mm + ":" + ss;
	}

	/**
	 * 日期轉星期。
	 * @return java.lang.String
	 * @param xdate java.lang.String (例如："20000620"或"2000/06/20")
	 * @exception java.io.IOException
	 */
	public int getWeek(String xdate) {

		try {
			if (xdate == null || xdate.length() == 0)
				return -1;
			xdate = xdate.trim();

			String wrk_date;
			String xdate2 = ""; // formatting xdate

			// 去除xdate的"/"
			for (int i = 0; i < xdate.length(); i++) {
				if (xdate.charAt(i) != '/') {
					xdate2 = xdate2 + xdate.charAt(i);
				}
			}
			int len = xdate.length();
			if (len > 8 || len < 6)
				return -1;

			xdate = xdate2;

			String days[] = new String[42];
			for (int i = 0; i < 42; i++) {
				days[i] = "";
			}
			int year = Integer.parseInt(xdate.substring(0, len - 4));
			int month = Integer.parseInt(xdate.substring(len - 4, len - 2)) - 1;
			int day = Integer.parseInt(xdate.substring(len - 2, len));

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.YEAR, year);
			calendar.setFirstDayOfWeek(Calendar.SUNDAY);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			int firstIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			int maxIndex = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

			int iWeek = -1;
			for (int i = 0; i < maxIndex; i++) {
				days[firstIndex + i] = String.valueOf(i + 1);
			}

			for (int j = 0; j < 6; j++) {
				int n = 0;
				for (int i = j * 7; i < (j + 1) * 7; i++) {
					if ((i - firstIndex + 1) == day) {
						iWeek = n;
					}
					n = n + 1;
				}
			}

			return iWeek;

		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * <PRE><CODE>
	 *  方法名稱︰日期格式轉換 
	 *  方法說明︰
	 *  範例︰輸入2001/01/01 輸出 20010101
	 *  @param
	 *  @return
	 *  @exception
	 *  @see also
	 *  @since
	 *  </CODE></PRE>
	 * @param i_date java.lang.String
	 */
	public String trimDate(String i_date) {

		if (i_date == null || i_date.length() == 0)
			return "";
		int len = i_date.length();
		String yy = i_date.substring(0, len - 6);
		String mm = i_date.substring(len - 5, len - 3);
		String dd = i_date.substring(len - 2, len);
		return yy + mm + dd;
	}

	/**
	 * <PRE><CODE>
	 *  方法名稱︰日期格式轉換 
	 *  方法說明︰
	 *  範例︰輸入2001/01/01 11:22:33 輸出 20010101112233
	 *  @param
	 *  @return
	 *  @exception
	 *  @see also
	 *  @since
	 *  </CODE></PRE>
	 * @param i_date java.lang.String
	 */
	public String trimTime(String i_date) {
		if (i_date == null || i_date.length() == 0) return "";
		String yy = i_date.substring(0, 4);
		String mm = i_date.substring(5, 7);
		String dd = i_date.substring(8, 10);
		String hr = i_date.substring(11, 13);
		String mi = i_date.substring(14, 16);
		String ss = i_date.substring(17, 19);
		return yy + mm + dd + hr + mi + ss;
	}
	
	/**
	 * 計算兩個日期的月份差(不管「日」的部份)
	 * @param i_date1
	 * @param i_date2
	 * @return
	 */
	public static int offsetMonth(String i_date1, String i_date2) {
		try {
			Calendar date1 = Calendar.getInstance();
			Calendar date2 = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

			date1.setTime(df.parse(i_date1));
			date2.setTime(df.parse(i_date2));

			int intYearOffset = date1.get(Calendar.YEAR) - date2.get(Calendar.YEAR);
			int intMonthOffset = date1.get(Calendar.MONTH) - date2.get(Calendar.MONTH);

			return Math.abs((intYearOffset * 12 + intMonthOffset));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 依據日期取得當約的週數
	 * @param i_date1
	 * @param i_date2
	 * @return
	 */
	public static int getWeekOfMonth() {
		Calendar cal = Calendar.getInstance();
		int weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);
		
		return weekOfMonth;
	}
	

	/**
	 * 計算一年中的第幾天
	 */
	public int getCalendarDay(String i_date) {

	String strDate =  getDateE2(i_date);
	strDate = strDate.concat("/"); //在字串最後面多加上一個「/」，方便拮取年、月、日
	String strTemp = ""; //暫時存放字串

	int month_day[] = new int[] {0,31,28,31,30,31,30,31,31,30,31,30,31}; //宣告每個月的天數，依序為1月、2月--->12月
	int ymd[] = new int[3]; //用來存放年、月、日
	int day_sum = 0;

	//把年、月、日分別取出放在ymd陣列
	int j=0;
	for(int i=0; i<strDate.length(); i++) {

	   if(strDate.charAt(i) != '/') {
		   strTemp += strDate.charAt(i);
	   } else {
   	      ymd[j++] = Integer.parseInt(strTemp);
   	      strTemp = "";
	   }
	 }

	//計算日期

	for(int m = 1; m < ymd[1]; m++)
	day_sum += month_day[m]; //加總月的天數
	day_sum += ymd[2]; //加上日的天數
	if( (( (ymd[0] % 4) == 0) && ((ymd[0] % 100) != 0)) || ((ymd[0] % 400) == 0) ) //判斷是否為閏年，是的話要再多加1天
	day_sum++;
    return day_sum;

	//顯示結果
	//System.out.println(ymd[0]+"年"+ymd[1]+"月"+ymd[2]+"日"+"是今年的第"+day_sum+"天");
   }


	/**
	 * <PRE><CODE>
	 *  方法名稱︰取得日期時間格式
	 *  方法說明︰
	 *  範例︰輸入20010101120000 輸出2002-08-20T11:28:56.000-08:00
	 *  @param
	 *  @return
	 *  @exception
	 *  @see also
	 *  @since
	 *  </CODE></PRE>
	 * @param i_date java.lang.String
	 */
	public String getDateTimeTimeZoneFmt(String i_datetime) {

		if (i_datetime == null || i_datetime.length() == 0)
			return null;
		int len = i_datetime.length();
		String year = i_datetime.substring(0, len - 10);
		String month = i_datetime.substring(len - 10, len - 8);
		String day = i_datetime.substring(len - 8, len - 6);
		String hh = i_datetime.substring(len - 6, len - 4);
		String mm = i_datetime.substring(len - 4, len - 2);
		String ss = i_datetime.substring(len - 2, len);
		return year + "-" + month + "-" + day + "T" + hh + ":" + mm + ":" + ss + ".000-08:00";
	}
	
	/**
	 * <PRE><CODE>
	 *  方法名稱︰取得日期格式
	 *  方法說明︰
	 *  範例︰輸入20010101 輸出2002-08-20
	 *  @param
	 *  @return
	 *  @exception
	 *  @see also
	 *  @since
	 *  </CODE></PRE>
	 * @param i_date java.lang.String
	 */
	public String getDateDeshFmt(String i_date) {
	
		if (i_date == null || i_date.length() == 0)
			return null;
		int len = i_date.length();
		String year = i_date.substring(0, len - 4);
		String month = i_date.substring(len - 4, len - 2);
		String day = i_date.substring(len - 2, len);

		return year + "-" + month + "-" + day;
	}
}