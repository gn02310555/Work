package com.fpg.ec.utility.servlet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fpg.ec.utility.DataCar;
/** 
 * <PRE><CODE> 
 * 作業機能：共用模組__資料車管理。 
 * 類別名稱：資料車(DataCar)處理者。
 * 類別功能：處理DataCar在Session中之動作。 
 * @author  PC18/吳安祥
 * </CODE></PRE> 
 */
public class DataCarProcessor {
	private HttpServletRequest request = null;
	private ArrayList alRoot = null;
	private String strDCName = null; // DataCar Name in Session
	private static String strUsage = "Usage : name => $dc$_<fieldname>_<index>{$<index>}";
	private HttpSession session = null;

	public DataCarProcessor() {
	}

	public DataCarProcessor(HttpServletRequest newRequest, String newDCName) throws Exception {
		this.request = newRequest;
		this.strDCName = "$DataCar$" + newDCName;
		session = request.getSession(false);
	}

	public DataCarProcessor(HttpSession newSession, String newDCName) throws Exception {
		this.strDCName = "$DataCar$" + newDCName;
		session = newSession;
	}

	/**
	 * <PRE><CODE>
	 * 將request的資料轉成DataCar前之必要資料檢查
	 * </CODE></PRE>
	 */
	private void validate1() throws Exception {
		StringBuffer strbReturn = new StringBuffer();
		if (this.request == null)
			strbReturn.append("request is null !! ");
		if (this.strDCName == null)
			strbReturn.append("DCName is null !! ");

		if (strbReturn.toString().length() > 0)
			throw new Exception(strbReturn.toString());
	}

	/**
	 * <PRE><CODE>
	 * 從session中取得或移除某DataCar前之必要資料檢查
	 * </CODE></PRE>
	 */
	private void validate2() throws Exception {
		StringBuffer strbReturn = new StringBuffer();
		if (this.session == null)
			strbReturn.append("session is null !! ");
		if (this.strDCName == null)
			strbReturn.append("DCName is null !! ");

		if (strbReturn.toString().length() > 0)
			throw new Exception(strbReturn.toString());
	}

	/**
	 * <PRE><CODE>
	 * 將request的資料轉成DataCar並置入session
	 * </CODE></PRE>
	 */
	public void convert() throws Exception {
		validate1();
		alRoot = (session.getAttribute(strDCName) == null) ? new ArrayList() : (ArrayList) ((DataCar) session.getAttribute(strDCName)).getAllData().clone();
		Enumeration enumNames = request.getParameterNames();
		while (enumNames.hasMoreElements()) { //get all parameters
			String strName = (String) enumNames.nextElement();
			String strValue = request.getParameter(strName).trim();
			if (strName.startsWith("$dc$")) { // choose the name belong to DataCar

				//split the name to Fieldname & IndexString
				StringTokenizer stkrName = new StringTokenizer(strName.substring(4, strName.length()), "_");

				// get Fieldname
				String strFieldname = "";
				if (stkrName.hasMoreTokens())
					strFieldname = stkrName.nextToken();
				if (strFieldname.length() == 0)
					throw new Exception("Error in [" + strName + "] " + strUsage);

				// get Index String ( Ex : 1.2.4 )
				String strIndexs = "";
				if (stkrName.hasMoreTokens())
					strIndexs = stkrName.nextToken();

				if (strIndexs.length() > 0) {
					StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "$");
					if (alRoot == null)
						alRoot = new ArrayList();
					ArrayList alTemp = alRoot; //point to current arraylist
					Hashtable hstTemp = null; //point to current hashtable

					while (stkrIndexs.hasMoreTokens()) {
						String strIndex = stkrIndexs.nextToken();
						int intIndex = -1;
						try {
							intIndex = Integer.parseInt(strIndex);
							try {
								hstTemp = (Hashtable) alTemp.get(intIndex);
								alTemp = (ArrayList) hstTemp.get("$detail$");
								if (alTemp == null) {
									hstTemp.put("$detail$", new ArrayList());
									alTemp = (ArrayList) hstTemp.get("$detail$");
								}
							} catch (IndexOutOfBoundsException e) {
								int intAlTempSize = alTemp.size();
								for (int i = intAlTempSize; i <= intIndex; i++) {
									alTemp.add(new Hashtable());
									hstTemp = (Hashtable) alTemp.get(i);
								}
								hstTemp = (Hashtable) alTemp.get(intIndex);
								alTemp = (ArrayList) hstTemp.get("$detail$");
								if (alTemp == null) {
									hstTemp.put("$detail$", new ArrayList());
									alTemp = (ArrayList) hstTemp.get("$detail$");
								}
							}
						} catch (NumberFormatException e) {
							throw new Exception("Error in [" + strName + "] Index nust be a number >= 0 !");
						}
					}
					if (hstTemp != null) {
						hstTemp.put(strFieldname.toLowerCase(), strValue);
						//System.out.println(strFieldname+","+strIndexs+" =>"+strValue);
					}
				} else {
					throw new Exception("Error in [" + strName + "] " + strUsage);
				} // end of // if (strIndexs.length() > 0 )
			} // end of // choose the name belong to DataCar
		} // end of //get all parameters
		session.setAttribute(strDCName, new com.fpg.ec.utility.DataCar(alRoot));
		alRoot = null;
	} // end of method doConvert()
	
	 /**
	  * <PRE><CODE>
	  * 將request的資料轉成DataCar並置入session
	  * 以dc_為DataCar識別碼,後方index的部也改為用"_"隔開
	  * </CODE></PRE>
	  */
	 public static DataCar transform(HttpServletRequest request) throws Exception {
	  //validate1();
	  ArrayList alRoot = new ArrayList();
	  Enumeration enumNames = request.getParameterNames();
	  while (enumNames.hasMoreElements()) { //get all parameters
	   String strName = (String) enumNames.nextElement();
	   String strValue = request.getParameter(strName).trim();
	   if (strName.startsWith("dc_")) { // choose the name belong to DataCar
	 
	    //split the name to Fieldname & IndexString
	    StringTokenizer stkrName = new StringTokenizer(strName.substring(3, strName.length()), "_");
	 
	    // get Fieldname
	    String strFieldname = "";
	    String strFormatMark = "";//資料轉換參數
	    if (stkrName.hasMoreTokens())
	     strFieldname = stkrName.nextToken();
	        /*
	        if( strFieldname.indexOf("$") >= 0){//表示有資料轉換參數
	            strFormatMark = strFieldname.substring(strFieldname.indexOf("$")+1);
	            strFieldname = strFieldname.substring(0,strFieldname.indexOf("$"));
	            if( "d".equalsIgnoreCase(strFormatMark) && strValue.length() > 0){// MM/DD/YYYY => YYYYMMDD
	                int intYear = Integer.parseInt(strValue.substring(6));
	                int  intDay = Integer.parseInt(strValue.substring(3,5));
	                int  intMonth = Integer.parseInt(strValue.substring(0,2))-1;//base 0
	                Calendar c = Calendar.getInstance();
	                c.set(intYear,intMonth,intDay);
	                if( intYear != c.get(Calendar.YEAR) || intMonth != c.get(Calendar.MONTH) || intDay != c.get(Calendar.DATE) ){
	                    throw new RuntimeException(strFieldname+" 日期格式錯誤");
	                }
	                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	                strValue = formatter.format(c.getTime());
	            }
	        }*/
	    if (strFieldname.length() == 0)
	     throw new Exception("Error in [" + strName + "] " + strUsage);
	 
	    // get Index String ( Ex : 1.2.4 )
	    /*
	    String strIndexs = "";
	    if (stkrName.hasMoreTokens())
	     strIndexs = stkrName.nextToken();
	    */
	 
	    //if (strIndexs.length() > 0) {
	    if (true) {
	     //StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "_");
	     StringTokenizer stkrIndexs = stkrName;
	     if (alRoot == null)
	      alRoot = new ArrayList();
	     ArrayList alTemp = alRoot; //point to current arraylist
	     Hashtable hstTemp = null; //point to current hashtable
	 
	     while (stkrIndexs.hasMoreTokens()) {
	      String strIndex = stkrIndexs.nextToken();
	      int intIndex = -1;
	      try {
	       intIndex = Integer.parseInt(strIndex);
	       try {
	        hstTemp = (Hashtable) alTemp.get(intIndex);
	        alTemp = (ArrayList) hstTemp.get("$detail$");
	        if (alTemp == null) {
	         hstTemp.put("$detail$", new ArrayList());
	         alTemp = (ArrayList) hstTemp.get("$detail$");
	        }
	       } catch (IndexOutOfBoundsException e) {
	        int intAlTempSize = alTemp.size();
	        for (int i = intAlTempSize; i <= intIndex; i++) {
	         alTemp.add(new Hashtable());
	         hstTemp = (Hashtable) alTemp.get(i);
	        }
	        hstTemp = (Hashtable) alTemp.get(intIndex);
	        alTemp = (ArrayList) hstTemp.get("$detail$");
	        if (alTemp == null) {
	         hstTemp.put("$detail$", new ArrayList());
	         alTemp = (ArrayList) hstTemp.get("$detail$");
	        }
	       }
	      } catch (NumberFormatException e) {
	       throw new Exception("Error in [" + strName + "] Index nust be a number >= 0 !");
	      }
	     }
	     if (hstTemp != null) {
	      hstTemp.put(strFieldname.toLowerCase(), strValue);
	      //System.out.println(strFieldname+","+strIndexs+" =>"+strValue);
	     }
	    } else {
	     throw new Exception("Error in [" + strName + "] " + strUsage);
	    } // end of // if (strIndexs.length() > 0 )
	   } // end of // choose the name belong to DataCar
	  } // end of //get all parameters
	 
	  return new com.fpg.ec.utility.DataCar(alRoot);
	 } // end of method doConvert()


	/**
	 * <PRE><CODE>
	 * 將request的資料轉成DataCar並置入session
	 * </CODE></PRE>
	 */
	public static DataCar convert(HttpServletRequest request) throws Exception {
		//validate1();
		ArrayList alRoot = new ArrayList();
		Enumeration enumNames = request.getParameterNames();
		while (enumNames.hasMoreElements()) { //get all parameters
			String strName = (String) enumNames.nextElement();
			String strValue = request.getParameter(strName).trim();
			if (strName.startsWith("$dc$")) { // choose the name belong to DataCar

				//split the name to Fieldname & IndexString
				StringTokenizer stkrName = new StringTokenizer(strName.substring(4, strName.length()), "_");

				// get Fieldname
				String strFieldname = "";
				String strFormatMark = "";//資料轉換參數
				if (stkrName.hasMoreTokens())
					strFieldname = stkrName.nextToken();
				    if( strFieldname.indexOf("$") >= 0){//表示有資料轉換參數
				        strFormatMark = strFieldname.substring(strFieldname.indexOf("$")+1);
				        strFieldname = strFieldname.substring(0,strFieldname.indexOf("$"));
				        if( "d".equalsIgnoreCase(strFormatMark) && strValue.length() > 0){// MM/DD/YYYY => YYYYMMDD
				            int intYear = Integer.parseInt(strValue.substring(6));
				            int  intDay = Integer.parseInt(strValue.substring(3,5));
				            int  intMonth = Integer.parseInt(strValue.substring(0,2))-1;//base 0
				            Calendar c = Calendar.getInstance();
				            c.set(intYear,intMonth,intDay);
				            if( intYear != c.get(Calendar.YEAR) || intMonth != c.get(Calendar.MONTH) || intDay != c.get(Calendar.DATE) ){
				                throw new RuntimeException(strFieldname+" 日期格式錯誤");
				            }
				            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				            strValue = formatter.format(c.getTime());
				        }
				    }
				if (strFieldname.length() == 0)
					throw new Exception("Error in [" + strName + "] " + strUsage);

				// get Index String ( Ex : 1.2.4 )
				String strIndexs = "";
				if (stkrName.hasMoreTokens())
					strIndexs = stkrName.nextToken();

				if (strIndexs.length() > 0) {
					StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "$");
					if (alRoot == null)
						alRoot = new ArrayList();
					ArrayList alTemp = alRoot; //point to current arraylist
					Hashtable hstTemp = null; //point to current hashtable

					while (stkrIndexs.hasMoreTokens()) {
						String strIndex = stkrIndexs.nextToken();
						int intIndex = -1;
						try {
							intIndex = Integer.parseInt(strIndex);
							try {
								hstTemp = (Hashtable) alTemp.get(intIndex);
								alTemp = (ArrayList) hstTemp.get("$detail$");
								if (alTemp == null) {
									hstTemp.put("$detail$", new ArrayList());
									alTemp = (ArrayList) hstTemp.get("$detail$");
								}
							} catch (IndexOutOfBoundsException e) {
								int intAlTempSize = alTemp.size();
								for (int i = intAlTempSize; i <= intIndex; i++) {
									alTemp.add(new Hashtable());
									hstTemp = (Hashtable) alTemp.get(i);
								}
								hstTemp = (Hashtable) alTemp.get(intIndex);
								alTemp = (ArrayList) hstTemp.get("$detail$");
								if (alTemp == null) {
									hstTemp.put("$detail$", new ArrayList());
									alTemp = (ArrayList) hstTemp.get("$detail$");
								}
							}
						} catch (NumberFormatException e) {
							throw new Exception("Error in [" + strName + "] Index nust be a number >= 0 !");
						}
					}
					if (hstTemp != null) {
						hstTemp.put(strFieldname.toLowerCase(), strValue);
						//System.out.println(strFieldname+","+strIndexs+" =>"+strValue);
					}
				} else {
					throw new Exception("Error in [" + strName + "] " + strUsage);
				} // end of // if (strIndexs.length() > 0 )
			} // end of // choose the name belong to DataCar
		} // end of //get all parameters

		return new com.fpg.ec.utility.DataCar(alRoot);
	} // end of method doConvert()
	/**
	 * <PRE><CODE>
	 * 從session中取得某DataCar
	 * </CODE></PRE>
	 */
	public DataCar getDataCar() throws Exception {
		validate2();
		DataCar dcTemp = (DataCar) session.getAttribute(strDCName);
		if (dcTemp == null) {
			//ArrayList alNew = new ArrayList();
			dcTemp = new DataCar(new ArrayList());
			session.setAttribute(strDCName, dcTemp);
			//dcTemp = (DataCar) session.getAttribute(strDCName);
		}
		return dcTemp;
	}

	/**
	 * <PRE><CODE>
	 * 從session中移除某DataCar
	 * </CODE></PRE>
	 */
	public void removeDataCar() throws Exception {
		validate2();
		session.removeAttribute(strDCName);
	}

}