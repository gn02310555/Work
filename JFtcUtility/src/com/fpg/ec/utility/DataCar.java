package com.fpg.ec.utility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class DataCar implements Serializable{
	private ArrayList alData;
	private Hashtable hstKey = new Hashtable();
	private Hashtable hstKeyGroup = new Hashtable();
	private DataCarSize dcs;

	public DataCar() {
	}

	public DataCar(ArrayList newAlData) {
		this.alData = newAlData;
		if (this.alData == null)
			System.out.println("DataCar's alData is null !!");
	}

	public DataCarSize getDataCarSize() {
		return new DataCarSize(this);
	}

	/**
	 * 取得所有資料
	 */
	public ArrayList getAllData() {
		if (alData == null){
			alData = new ArrayList();
		}	
		return alData;
	}

	/**
	 * 取得一筆資料
	 * @param strIndexs : 欲取得資料之index
	 */
	public Hashtable getARecord(String strIndexs) {
		ArrayList alTemp = getAllData();
		Hashtable hstTemp = null;
		StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "$");
		while (stkrIndexs.hasMoreTokens()) {
			String strIndex = stkrIndexs.nextToken();
			int intIndex = -1;
			try {
				intIndex = Integer.parseInt(strIndex);
				try {
					if (alTemp == null) {
						return null;
					}
					hstTemp = (Hashtable) alTemp.get(intIndex);
					if (hstTemp == null) {
						return null;
					}
					alTemp = (ArrayList) hstTemp.get("$detail$");
					
				} catch (IndexOutOfBoundsException e) {
					return null;
				}
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return hstTemp;
	}

	/**
	 * 取得一組資料
	 * @param strIndexs : 欲取得資料之index
	 */
	public ArrayList getAList(String strIndexs) {
		ArrayList alTemp = getAllData();
		Hashtable hstTemp = null;
		if (strIndexs == null || strIndexs.length() == 0)
			return alTemp;
		StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "$");
		while (stkrIndexs.hasMoreTokens()) {
			String strIndex = stkrIndexs.nextToken();
			int intIndex = -1;
			try {
				intIndex = Integer.parseInt(strIndex);
				try {
					if (alTemp == null) {
						return null;
					}
					hstTemp = (Hashtable) alTemp.get(intIndex);
					if (hstTemp == null) {
						return null;
					}
					alTemp = (ArrayList) hstTemp.get("$detail$");
					
				} catch (IndexOutOfBoundsException e) {
					return null;
				}
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return alTemp;
	}
	
	/**
	 * 設定某一筆資料的欄位值
	 * @param i_fn : 欄位名
	 * @param i_value : 欄位值 
	 * @param strIndexs : 資料indwc
	 * @throws Exception
	 */
	public void setValue(String i_fn, String i_value, String strIndexs ) throws Exception {
		Hashtable hstARecord = getARecord(strIndexs);
		if( hstARecord == null ) {
			setARecord(new Hashtable(), strIndexs);
			hstARecord = getARecord(strIndexs);
		} 
		hstARecord.put(i_fn.toLowerCase(),i_value);
	}
	
	/**
	 * 設定一筆資料
	 * @param strIndexs : 欲設定資料之index
	 */
	public void setARecord(Hashtable hstARecord, String strIndexs) throws Exception {
		ArrayList alTemp = getAllData();
		Hashtable hstTemp = null;
		if (strIndexs == null)
			strIndexs = "";
		StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "$");
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
					}
					hstTemp = (Hashtable) alTemp.get(intIndex);
					alTemp = (ArrayList) hstTemp.get("$detail$");
					if (alTemp == null) {
						hstTemp.put("$detail$", new ArrayList());
						alTemp = (ArrayList) hstTemp.get("$detail$");
					}
				}
			} catch (NumberFormatException e) {
				throw new Exception("Error in [setARecord Method] Index nust be a number >= 0 !");
			}
		}
		if (hstTemp != null){
			hstTemp.clear();
			hstTemp.putAll(hstARecord);
		}else
			throw new Exception("Error in [setARecord Method] Can't set the record to the index (" + strIndexs + ")!");
	}
	/**
	 * 在某一筆資料中補上另一組資料,若資料欄同名則overwrite
	 * @param strIndexs : 欲設定資料之index
	 */
	public void appARecord(Hashtable hstARecord, String strIndexs) throws Exception {
		ArrayList alTemp = getAllData();
		Hashtable hstTemp = null;
		if (strIndexs == null)
			strIndexs = "";
		StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "$");
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
					}
					hstTemp = (Hashtable) alTemp.get(intIndex);
					alTemp = (ArrayList) hstTemp.get("$detail$");
					if (alTemp == null) {
						hstTemp.put("$detail$", new ArrayList());
						alTemp = (ArrayList) hstTemp.get("$detail$");
					}
				}
			} catch (NumberFormatException e) {
				throw new Exception("Error in [setARecord Method] Index nust be a number >= 0 !");
			}
		}
		if (hstTemp != null)
			hstTemp.putAll(hstARecord);
		else
			throw new Exception("Error in [setARecord Method] Can't set the record to the index (" + strIndexs + ")!");
	}
	/**
	 * 移除一筆資料
	 * @param strIndexs : 欲刪除資料之index
	 */
	public void removeARecord(String strIndexs) {
		ArrayList alTemp = getAllData();
		Hashtable hstTemp = null;
		ArrayList alParent = null;
		int intRemoveIndex = -1;
		StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "$");
		while (stkrIndexs.hasMoreTokens()) {
			String strIndex = stkrIndexs.nextToken();
			int intIndex = -1;
			try {
				intIndex = Integer.parseInt(strIndex);
				try {
					if (alTemp == null) {
						return;
					}
					hstTemp = (Hashtable) alTemp.get(intIndex);
					alParent = alTemp;
					intRemoveIndex = intIndex;
					alTemp = (ArrayList) hstTemp.get("$detail$");
					
				} catch (IndexOutOfBoundsException e) {
					return;
				}
			} catch (NumberFormatException e) {
				return;
			}
		}
	    alParent.remove(intRemoveIndex);
	}
	
	/**
	 * 增加一筆資料
	 * @param strIndexs : 欲增加資料之index
	 */
	public void addARecord(String strIndexs) {
		ArrayList alTemp = getAllData();
		Hashtable hstTemp = null;
		StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "$");
		while (stkrIndexs.hasMoreTokens()) {
			String strIndex = stkrIndexs.nextToken();
			int intIndex = -1;
			try {
				intIndex = Integer.parseInt(strIndex);
				try {
					hstTemp = (Hashtable) alTemp.get(intIndex);
					alTemp = (ArrayList) hstTemp.get("$detail$");
					if (alTemp == null) {
						alTemp = new ArrayList();
						hstTemp.put("$detail$",alTemp);
					}
				} catch (IndexOutOfBoundsException e) {
					return;
				}
			} catch (NumberFormatException e) {
				return;
			}
		}
	    Hashtable hstNew = new Hashtable();
	    alTemp.add(hstNew);
	}


	/**
	 * 設定一組資料
	 * @param strIndexs : 欲設定資料之index
	 */
	public void setAList(ArrayList alAList, String strIndexs) throws Exception {
		ArrayList alTemp = getAllData();
		Hashtable hstTemp = null;
		if (strIndexs == null)
			strIndexs = "";
		StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "$");
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
					}
					hstTemp = (Hashtable) alTemp.get(intIndex);
					alTemp = (ArrayList) hstTemp.get("$detail$");
					if (alTemp == null) {
						hstTemp.put("$detail$", new ArrayList());
						alTemp = (ArrayList) hstTemp.get("$detail$");
					}
				}
			} catch (NumberFormatException e) {
				throw new Exception("Error in [setAList Method] Index nust be a number >= 0 !");
			}
		}
		/* for debug ...
		System.out.println("s1"+alTemp.size());
		for( int i = 0; i<alTemp.size(); i++){
			Hashtable ht = (Hashtable)alTemp.get(i);
			Enumeration enum = ht.keys();
			String str = "";
			while( enum.hasMoreElements() )
			str += (String)enum.nextElement()+"|";
			System.out.println(str);
		}*/
		alTemp.clear();
		alTemp.addAll(alAList);
		
		
	}

	/**
	 * 取得某一筆資料之欄位值
	 * @param strFieldname : 欲取得資料之欄位名
	 * @param strIndexs : 欲取得資料之index
	 */
	public String getValue(String strFieldname, String strIndexs) {
		Hashtable hstTemp = getARecord(strIndexs);
		if (hstTemp == null){
			return "";
		} else
			return (hstTemp.get(strFieldname.toLowerCase()) == null) ? "" : ((String) hstTemp.get(strFieldname.toLowerCase())).trim();
	}

	/**
	 * 取得某一組資料之筆數
	 * @param strIndexs : 欲取得資料筆數之index
	 */
	public int getSize(String strIndexs) throws Exception {
		ArrayList alTemp = getAllData();
		Hashtable hstTemp = null;
		if (strIndexs == null)
			strIndexs = "";
		StringTokenizer stkrIndexs = new StringTokenizer(strIndexs, "$");
		while (stkrIndexs.hasMoreTokens()) {
			String strIndex = stkrIndexs.nextToken();
			int intIndex = -1;
			try {
				intIndex = Integer.parseInt(strIndex);
				try {
					hstTemp = (Hashtable) alTemp.get(intIndex);
					alTemp = (ArrayList) hstTemp.get("$detail$");
				} catch (IndexOutOfBoundsException e) {
					throw new Exception("Error in [getSize Method] Index is out of range !");
				}
			} catch (NumberFormatException e) {
				throw new Exception("Error in [getSize Method] Index nust be a number >= 0 !");
			}
		}
		return alTemp.size();

	}

	/**
	 * 轉換系統換行字元為DataCar換行字元特殊Tag
	 * @param strDCValue : 欲轉換換行字元之DataCar欄位值
	 */
	public static String convertNewLineToTag(String strDCValue) {
		return new StringUtil().replaceWord(strDCValue, "\r\n", "$nl$");
	}

	/**
	 * 轉換DataCar換行字元特殊Tag為系統換行字元
	 * @param strDCValue : 欲轉換換行字元之DataCar欄位值
	 */
	public static String convertNewLineTag(String strDCValue) {
		return new StringUtil().replaceWord(strDCValue, "$nl$", "\r\n");
	}

	/**
	 * 設定必要欄位名稱
	 * @param strKey : 必要欄位名稱
	 * @param strIndexs : 必要欄位所屬資料群之Index
	 */
	public void addKey(String strKey, String strIndexs) throws Exception {
		if (strKey.indexOf(",") >= 0)
			throw new Exception("Error in [addKey Method] Key name cannot contain comma『,』 !");
		String strTmp = (String) this.hstKey.get(strIndexs);
		if (strTmp == null) {
			this.hstKey.put(strIndexs, strKey);
		} else {
			this.hstKey.put(strIndexs, strTmp + "," + strKey);
		}
	}

	/**
	 * 設定必要欄位名稱組
	 * @param strKey : 必要欄位名稱
	 * @param strIndexs : 必要欄位組所屬資料群之Index
	 */
	public void addKeyGroup(String strKey, String strIndexs) throws Exception {
		if (strKey.indexOf(",") >= 0)
			throw new Exception("Error in [addKeyGroup Method] Key name cannot contain comma『,』 !");
		String strTmp = (String) this.hstKeyGroup.get(strIndexs);
		if (strTmp == null) {
			this.hstKeyGroup.put(strIndexs, strKey);
		} else {
			this.hstKeyGroup.put(strIndexs, strTmp + "," + strKey);
		}
	}

	/**
	 * 清除未輸入必要欄位及必要欄位組之資料項
	 */
	public void clearWithKeys() throws Exception {
		//clear with keys
		clearWithKey();
		//clear with key group
		clearWithKeyGroup();
	}

	/**
	 * 清除未輸入必要欄位之資料項
	 */
	private void clearWithKey() throws Exception {

		Enumeration enumTmp = hstKey.keys();
		while (enumTmp.hasMoreElements()) {
			String strIndexs = (String) enumTmp.nextElement();
			String strKeys = (String) this.hstKey.get(strIndexs);
			if (strKeys == null || strKeys.length() == 0)
				return;
			int intStarPos = strIndexs.indexOf("*");
			if (intStarPos >= 0) {
				String strCompare = strIndexs.substring(0, intStarPos);
				DataCarSize dcs = new DataCarSize(this);
				Enumeration enumValiIndex = dcs.getHstSize().keys();
				while (enumValiIndex.hasMoreElements()) {
					String strValiIndex = (String) enumValiIndex.nextElement(); //有效的IndexString
					//System.out.println("v:"+strValiIndex);
					//System.out.println("c:"+strCompare);
					if (strCompare.equals("") || strValiIndex.startsWith(strCompare)) {
						if (!strValiIndex.equals("")) {
							clearWithKey(strValiIndex, strKeys);
						}
					}
				}
			} else {
				clearWithKey(strIndexs, strKeys);
			}
		} //end of while( enumTmp.6() )
	}

	/**
	 * 清除未輸入必要欄位之資料項(單一Indexs)
	 */
	private void clearWithKey(String strIndexs, String strKeys) {
		ArrayList alList = this.getAList(strIndexs);

		if (alList == null)
			return;
		
		StringTokenizer stkrKeys = new StringTokenizer(strKeys, ",");
		while (stkrKeys.hasMoreTokens()) {
			String strKey = stkrKeys.nextToken();
			//System.out.println("clearWithKey:"+strKey);
			int intListSize = alList.size();
			for (int i = intListSize - 1; i >= 0; i--) {
				
				Hashtable hstTmp = (Hashtable) alList.get(i);
				if (hstTmp == null) {
					alList.remove(i);
					continue;
				}
				String strValue = (String) hstTmp.get(strKey);
				if (strValue == null || strValue.trim().length() == 0){
					//System.out.println("remove>>"+i);
					alList.remove(i);
				}	
			}
		} //end of while(strkKeys.hasMoreTokens())

	}

	/**
	 * 清除未輸入必要欄位組之資料項
	 */
	private void clearWithKeyGroup() throws Exception {
		Enumeration enumTmp = hstKeyGroup.keys();
		while (enumTmp.hasMoreElements()) {
			String strIndexs = (String) enumTmp.nextElement();
			String strKeys = (String) this.hstKeyGroup.get(strIndexs);
			if (strKeys == null || strKeys.length() == 0)
				return;
			int intStarPos = strIndexs.indexOf("*");
			if (intStarPos >= 0) {
				String strCompare = strIndexs.substring(0, intStarPos);
				DataCarSize dcs = new DataCarSize(this);
				Enumeration enumValiIndex = dcs.getHstSize().keys();
				while (enumValiIndex.hasMoreElements()) {
					String strValiIndex = (String) enumValiIndex.nextElement(); //有效的IndexString
					if (strCompare.equals("") || strValiIndex.startsWith(strCompare)) {
						if (!strValiIndex.equals("")) {
							clearWithKeyGroup(strValiIndex, strKeys);
						}
					}
				}
			} else {
				clearWithKeyGroup(strIndexs, strKeys);
			}
		}
	}

	/**
	 * 清除未輸入必要欄位組之資料項(單一Index)
	 */
	private void clearWithKeyGroup(String strIndexs, String strKeys) {
		ArrayList alList = this.getAList(strIndexs);
		if (alList == null)
			return;

		int intListSize = alList.size();
		for (int i = intListSize - 1; i >= 0; i--) {
			Hashtable hstTmp = (Hashtable) alList.get(i);
			if (hstTmp == null) {
				alList.remove(i);
				continue;
			}

			StringTokenizer stkrKeys = new StringTokenizer(strKeys, ",");
			int intKeysNum = 0; //必要欄位個數
			int intNotContainNum = 0; //該筆資料不含必要欄位之個數
			while (stkrKeys.hasMoreTokens()) {
				String strKey = stkrKeys.nextToken();
				String strValue = (String) hstTmp.get(strKey);
				intKeysNum++;
					
				if (strValue == null || strValue.trim().length() == 0)
					intNotContainNum++;
			}
			if (intKeysNum > 0 && intKeysNum == intNotContainNum) {
				alList.remove(i);
			}
		} // end of for( int i=intListSize ; i>0; i--){
	}
}