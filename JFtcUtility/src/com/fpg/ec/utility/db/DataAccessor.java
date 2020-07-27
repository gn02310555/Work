package com.fpg.ec.utility.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;

import com.fpg.ec.utility.DataCar;
import com.fpg.ec.utility.ObjectUtil;
import com.fpg.ec.utility.StringUtil;

public abstract class DataAccessor implements Serializable {
	private static final long serialVersionUID = -507045695553669879L;

	private boolean isForceTransformUTF8 = false; //是否&#xxxxxx;轉回UTF字集

	protected DataCar dcData = new DataCar();
	private HashMap hmNeed2TransUTFRealStringField = new HashMap();
	private HashMap limitAccessibleFields = new HashMap(); //限制存取之欄位,設定後,getValue(fn)之fn必須在此範圍,否則回傳null
	private HashMap returnNullFields = new HashMap(); //傳回null之欄位,設定後,getValue(fn)之fn在此範圍即回傳null

	private boolean isReturnNullNoThisField = false;// 如果底層hashtable無此欄位 回傳null
	private boolean isReturnNullIfThisFieldEmpty = false;// 如果底層hashtable無此欄位 或是有此欄位但為空字串 回傳null(for SqlServer 數字欄位不能為空字串)

	private static final String INDEX_MASTER = "0";

	public DataAccessor() {
		setData(new Hashtable());
		initNeed2TransUTFRealStringField();
	}

	public DataAccessor(Hashtable i_data) {
		setData(i_data);
		initNeed2TransUTFRealStringField();
	}

	/**
	 * 子類別可實作此method以設定需轉為unicode字符的欄位
	 */
	protected void initNeed2TransUTFRealStringField() {
		/*
		 * sample
		 * addNeed2TransUTFRealStringField("field1");
		 * addNeed2TransUTFRealStringField("field2");
		 */
	}

	public void setData(Hashtable i_data) {
		try {
			dcData.setARecord(i_data, INDEX_MASTER);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 補充主檔資料
	 * 
	 * @param i_data
	 *            : 主檔資料
	 */
	public void appendData(Hashtable i_data) {
		try {
			dcData.appARecord(i_data, INDEX_MASTER);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 取得主檔欄位值
	 * 
	 * @param i_fn
	 *            : 欄位名
	 * @return String 主檔欄位值
	 */
	public String getValue(String i_fn) {
		String strReturn = dcData.getValue(i_fn, INDEX_MASTER);
		if (isForceTransformUTF8()) {
			if (strReturn.trim().length() > 0) {
				strReturn = new StringUtil().convertUnicodeField(strReturn);
			}
		} else {
			if (this instanceof Need2TransUTFRealString) {
				if (strReturn.trim().length() > 0) {
					strReturn = new StringUtil().convertNonBig52UnicodeRealString(strReturn);
				}
			} else if (isNeed2TransUTFRealStringField(i_fn)) {
				if (strReturn.trim().length() > 0) {
					strReturn = new StringUtil().convertNonBig52UnicodeRealString(strReturn);
				}
			}
		}

		if (!this.limitAccessibleFields.isEmpty()) {
			Hashtable hstTemp = dcData.getARecord(INDEX_MASTER);
			if (hstTemp == null) {
				return null;
			}
			if (this.limitAccessibleFields.containsKey(i_fn.toLowerCase())) {
				//pass
			} else {
				return null;
			}
		}

		if (!this.returnNullFields.isEmpty()) {
			Hashtable hstTemp = dcData.getARecord(INDEX_MASTER);
			if (hstTemp == null) {
				return null;
			}
			if (this.returnNullFields.containsKey(i_fn.toLowerCase())) {
				return null;
			}
		}

		if (isReturnNullNoThisField) {
			Hashtable hstTemp = dcData.getARecord(INDEX_MASTER);
			if (hstTemp == null) {
				return null;
			}
			if (hstTemp.get(i_fn.toLowerCase()) == null) {
				return null;
			}
		}
		
		if (isReturnNullIfThisFieldEmpty) {
			Hashtable hstTemp = dcData.getARecord(INDEX_MASTER);
			if (hstTemp == null) {
				return null;
			}
			if (hstTemp.get(i_fn.toLowerCase()) == null) {
				return null;
			}
			if (strReturn.length() == 0) {
				return null;
			}
		}		
		
		return strReturn;
	}

	public boolean isReturnNullNoThisField() {
		return isReturnNullNoThisField;
	}

	/**
	 * 原getValue()在datacar無此欄位時,會回空字串
	 * 此設定可使得當DataAccessor之datacar無此欄位時,會回傳null	
	 */
	public void setReturnNullNoThisField(boolean isReturnNullNoThisField) {
		this.isReturnNullNoThisField = isReturnNullNoThisField;
	}
	
	public boolean isReturnNullIfThisFieldEmpty() {
		return isReturnNullIfThisFieldEmpty;
	}

	/**
	 * 原getValue()在datacar無此欄位時,會回空字串
	 * 此設定可使得當DataAccessor之datacar無此欄位時,會回傳null
	 * 並如果該欄位為空字串 也會回傳null
	 */
	public void setReturnNullIfThisFieldEmpty(boolean isReturnNullIfThisFieldEmpty) {
		this.isReturnNullIfThisFieldEmpty = isReturnNullIfThisFieldEmpty;
	}
	
	

	/**
	 * 設定主檔資料
	 * 
	 * @param i_fn
	 *            : 欄位名稱
	 * @param i_value
	 *            : 欄位值
	 */
	public void setValue(String i_fn, String i_value) {
		Hashtable hstTemp = dcData.getARecord(INDEX_MASTER);
		if (i_value == null)
			i_value = "";
		hstTemp.put(i_fn, i_value);
	}

	/**
	 * 取得主檔資料
	 */
	public Hashtable getData() {
		Hashtable hstReturn = null;
		try {
			hstReturn = dcData.getARecord(INDEX_MASTER);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return hstReturn;
	}

	public String toString() {
		return dcData.getARecord(INDEX_MASTER).toString();
	}

	/**
	 * 取得主檔資料
	 */
	public Hashtable getDataClone() {
		Hashtable hstReturn = null;
		try {
			hstReturn = dcData.getARecord(INDEX_MASTER);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return (Hashtable) hstReturn.clone();
	}

	public Object getDeepClone() {
		try {
			return new ObjectUtil().deepCopy(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addNeed2TransUTFRealStringField(String i_fn) {
		this.hmNeed2TransUTFRealStringField.put(i_fn, "");
	}

	public void removeNeed2TransUTFRealStringField(String i_fn) {
		this.hmNeed2TransUTFRealStringField.remove(i_fn);
	}

	private boolean isNeed2TransUTFRealStringField(String i_fn) {
		return this.hmNeed2TransUTFRealStringField.containsKey(i_fn);
	}

	public boolean isForceTransformUTF8() {
		return isForceTransformUTF8;
	}

	public void setForceTransformUTF8(boolean isForceTransformUTF8) {
		this.isForceTransformUTF8 = isForceTransformUTF8;
	}

	public boolean isEmpty() {
		Hashtable hstTemp = dcData.getARecord(INDEX_MASTER);
		return (hstTemp == null || hstTemp.isEmpty());
	}

	/**
	 * 移除主檔資料之欄位
	 * 配合setReturnNullNoThisField(),可使已移除之欄位在透過getValue(fn)取值時得到null,
	 * 以配合DAO對應之xml中<isNull>的使用
	 * @param iFieldname: 欄位名,程式會自動轉小寫
	 */
	public void removeField(String iFieldname) {
		Hashtable hstTemp = dcData.getARecord(INDEX_MASTER);
		hstTemp.remove(iFieldname.toLowerCase());
	}
	/**
	 * 移除主檔資料之欄位(以「,」逗號隔開)
	 * @param iFieldnamesStr:欄位名字串,各欄位名稱間用「,」隔開,程式會自動轉小寫
	 */
	public void removeFields(String iFieldnamesStr) {
		String[] iFieldnames = iFieldnamesStr.split(",");
		for (int i = 0; i < iFieldnames.length; i++) {
			removeField(iFieldnames[i].trim().toLowerCase());
		}
	}

	/**
	 * 設定限制存取之欄位,
	 * 若getValue()不在此設定欄位中,會回傳null
	 * @param iFields : 欄位名陣列,程式會自動轉小寫
	 * @see clearLimitAccessibleFields
	 */
	public void setLimitAccessibleFieldsByArray(String[] iFields) {
		clearLimitAccessibleFields();
		for (int i = 0; i < iFields.length; i++) {
			this.limitAccessibleFields.put(iFields[i].trim().toLowerCase(), "");
		}
	}

	/**
	 * 設定限制存取之欄位,
	 * 若getValue()不在此設定欄位中,會回傳null
	 * @param iFields : 欄位名字串,各欄位名稱間用「,」隔開,程式會自動轉小寫
	 * @see clearLimitAccessibleFields
	 */
	public void setLimitAccessibleFields(String iFields) {
		setLimitAccessibleFieldsByArray(iFields.split(","));
	}

	/**
	 * 清除設定可存取之欄位
	 * @see setLimitAccessibleFields
	 */
	public void clearLimitAccessibleFields() {
		this.limitAccessibleFields = new HashMap();
	}
	
	/**
	 * 設定回傳null之欄位
	 * 若getValue()在此設定欄位中,會回傳null
	 * @param iFields : 欄位名陣列,程式會自動轉小寫
	 * @see clearReturnNullFields
	 */
	public void setReturnNullFieldsByArray(String[] iFields) {
		clearReturnNullFields();
		for (int i = 0; i < iFields.length; i++) {
			this.returnNullFields.put(iFields[i].trim().toLowerCase(), "");
		}
	}
	
	/**
	 * 設定回傳null之欄位,
	 * 若getValue()在此設定欄位中,會回傳null
	 * @param iFields : 欄位名字串,各欄位名稱間用「,」隔開,程式會自動轉小寫
	 * @see clearReturnNullFields
	 */
	public void setReturnNullFields(String iFields) {
		setReturnNullFieldsByArray(iFields.split(","));
	}

	/**
	 * 清除設定回傳null之欄位
	 * @see setReturnNullFields
	 */
	public void clearReturnNullFields() {
		this.returnNullFields = new HashMap();
	}
}
