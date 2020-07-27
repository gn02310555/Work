package com.fpg.ec.utility.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.fpg.ec.utility.ObjectUtil;

/**
 * 多重資料存取者(For iBATIS)
 * @author Evan
 */
public abstract class DataAccessorList implements Serializable {
	private static final long serialVersionUID = 6256469401847509436L;
	protected ArrayList list = new ArrayList();
	protected HashMap map = new HashMap();
	private HashMap uniqueMap = new HashMap();
	protected ArrayList sortFieldList = new ArrayList();

	/**
	 * sort方式
	 */
	public static final String SORT_TYPE_DEFAULT = "DEFAULT";//預設為文字排序方式
	public static final String SORT_TYPE_LENGTH = "LENGTH";//預設為文字長度排序方式

	public void clear() {
		this.list = new ArrayList();
		this.map = new HashMap();
		this.uniqueMap = new HashMap();
	}

	public void set(int i_index, Object i_obj) {
		this.list.set(i_index, i_obj);
	}

	public void add(Object i_obj) {
		if (i_obj instanceof DataAccessor) {
			if (!containsKey(getKey(i_obj))) {
				map.put(getKey(i_obj), i_obj);
				list.add(i_obj);
				uniqueMap.put(getUniqueKeyFromChild(i_obj), i_obj);

				if (this instanceof AddObjectExtension) {
					((AddObjectExtension) this).addExtensionTask((DataAccessor) i_obj);
				}
			}
		} else if (i_obj instanceof Hashtable) {
			DataAccessor daTemp = genObj((Hashtable) i_obj);
			if (!containsKey(getKey(daTemp))) {
				map.put(getKey(daTemp), daTemp);
				list.add(daTemp);
				uniqueMap.put(getUniqueKeyFromChild(daTemp), daTemp);

				if (this instanceof AddObjectExtension) {
					((AddObjectExtension) this).addExtensionTask(daTemp);
				}
			}
		} else {
			throw new RuntimeException(" add to MDataAccessor object must be DataAccessor or Hashtable " + i_obj.getClass());
		}
	}

	public void add(int i_index, Object i_obj) {
		if (i_obj instanceof DataAccessor) {
			if (!containsKey(getKey(i_obj))) {
				map.put(getKey(i_obj), i_obj);
				list.add(i_index, i_obj);
				uniqueMap.put(getUniqueKeyFromChild(i_obj), i_obj);
			}
		} else if (i_obj instanceof Hashtable) {
			DataAccessor daTemp = genObj((Hashtable) i_obj);
			if (!containsKey(getKey(daTemp))) {
				map.put(getKey(daTemp), daTemp);
				list.add(i_index, daTemp);
				uniqueMap.put(getUniqueKeyFromChild(daTemp), daTemp);
			}
		} else {
			throw new RuntimeException(" add to MDataAccessor object must be DataAccessor or Hashtable " + i_obj.getClass());
		}
	}

	/**
	 * 20091229 evan added for 繼承類別問題(父類別無法cast成子類別)
	 */
	public void add(Object i_obj, String i_className) {
		if (i_obj instanceof DataAccessor) {
			if (!containsKey(getKey(i_obj))) {
				DataAccessor daTemp = null;
				try {
					//比對是否為i_className之instance,否則轉成DataAccessor
					if (!Class.forName(i_className).isInstance(i_obj)) {
						daTemp = genObj(((DataAccessor) i_obj).getData());
					}
				} catch (Exception e) {
					throw new RuntimeException();
				}

				if (daTemp == null) {
					map.put(getKey(i_obj), i_obj);
					list.add(i_obj);
				} else {
					map.put(getKey(daTemp), daTemp);
					list.add(daTemp);
				}
			}
		} else {
			throw new RuntimeException(" add to MDataAccessor object must be DataAccessor or Hashtable " + i_obj.getClass());
		}
	}

	public int getIndex(Object i_obj) {
		int intReturn = -1;
		for (int i = 0; i < size(); i++) {
			if (equals(i, i_obj)) {
				intReturn = i;
				break;
			}
		}
		if (intReturn < 0)
			throw new RuntimeException("No this object in this MDataAccessor ! obj : " + i_obj);
		return intReturn;
	}

	private boolean equals(int i_index, Object i_obj) {
		return getKey(i_index).equals(getKey(i_obj));
	}

	public Object get(int i_index) {
		Object objReturn = list.get(i_index);
		if (objReturn == null) {
			return genObj(new Hashtable());
		}
		return objReturn;
	}

	public Object get(String i_key) {
		Object objReturn = map.get(i_key);
		if (objReturn == null) {
			return genObj(new Hashtable());
		}
		return objReturn;
	}

	public DataAccessor getObjectByUK(String i_key) {
		DataAccessor objReturn = (DataAccessor) uniqueMap.get(i_key);
		if (objReturn == null) {
			return genObj(new Hashtable());
		}
		return objReturn;
	}

	public Object getDeepClone() {
		try {
			return new ObjectUtil().deepCopy(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object getDeepClone(int i_index) {
		Object objReturn = get(i_index);
		try {
			return new ObjectUtil().deepCopy(objReturn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object getDeepClone(String i_key) {
		Object objReturn = get(i_key);
		try {
			return new ObjectUtil().deepCopy(objReturn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void remove(int i_index) {
		DataAccessor daObj = (DataAccessor) list.get(i_index);
		if (daObj != null) {
			map.remove(getKey(daObj));
			list.remove(i_index);
			uniqueMap.remove(getUniqueKeyFromChild(daObj));
		}
	}

	/**
	 * 20100818 dashontsai added for 可用KEY移除資料
	 */
	public void remove(String i_key) {
		DataAccessor daObj = (DataAccessor) get(i_key);
		if (daObj != null) {
			map.remove(getKey(daObj));
			list.remove(getIndex(daObj));
			uniqueMap.remove(getUniqueKeyFromChild(daObj));
		}
	}

	/**
	 * 20100818 dashontsai added for 可用物件移除
	 */
	public void remove(Object i_obj) {
		if (containsObj(i_obj)) {
			map.remove(getKey(i_obj));
			list.remove(getIndex(i_obj));
			uniqueMap.remove(getUniqueKeyFromChild(i_obj));
		}
	}

	public String getKey(int i_index) {
		DataAccessor daObj = (DataAccessor) list.get(i_index);
		if (daObj != null) {
			return getKey(daObj);
		}
		return null;
	}

	public void addAll(ArrayList i_list) {
		for (int i = 0; i < i_list.size(); i++) {
			Object obj = i_list.get(i);
			add(obj);
		}
	}

	// for iBATIS
	public void addAll(List i_list) {
		ArrayList arrayList = new ArrayList();
		arrayList.addAll(i_list);
		addAll(arrayList);
	}

	// for iBATIS 轉型
	public void addAll(List i_list, String i_className) {
		ArrayList arrayList = new ArrayList();
		arrayList.addAll(i_list);
		for (int i = 0; i < arrayList.size(); i++) {
			Object obj = i_list.get(i);
			add(obj, i_className);
		}
	}

	public int size() {
		return list.size();
	}

	public boolean containsObj(Object i_obj) {
		String strKey = "";
		try {
			strKey = getKey(i_obj);
		} catch (Exception e) {

		}
		return map.containsKey(strKey);
	}

	public boolean containsKey(String i_key) {
		return map.containsKey(i_key.trim());
	}

	public boolean containsUniqueKey(String i_key) {
		return uniqueMap.containsKey(i_key.trim());
	}

	public abstract String getKey(Object i_obj);

	public DataAccessor genObj(Hashtable i_obj) {
		throw new RuntimeException("please implement genObj() method in MDataAccessor !");
	}

	public String toString() {
		StringBuffer strbReturn = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			strbReturn.append("\nKey:" + getKey(i) + ",Map:" + map.get(getKey(i)) + ",Content:" + get(i));
		}
		return strbReturn.toString() + "\n";
	}

	public ArrayList getArrayListDeepClone() {
		try {
			ArrayList alReturn = (ArrayList) (new ObjectUtil().deepCopy(this.list));
			return alReturn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object[] getArrayDeepClone() {
		try {
			ArrayList alTemp = (ArrayList) (new ObjectUtil().deepCopy(this.list));
			return alTemp.toArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getUniqueKeyFromChild(Object i_obj) {
		if (i_obj instanceof Hashtable) {
			i_obj = genObj((Hashtable) i_obj);
		}

		if (this instanceof HasUniqueKey) {
			return ((HasUniqueKey) this).getUniqueKey((DataAccessor) i_obj);
		}

		return "";
	}

	public void sort() {
		Comparator comparator = new DefaultComparator(this.sortFieldList);
		ArrayList tmpList = list;
		this.clear();
		Collections.sort(tmpList, comparator);
		for (int i = 0; i < tmpList.size(); i++) {
			this.add(tmpList.get(i));
		}
	}

	public void sortDesc() {
		Comparator comparator = new DefaultComparator(this.sortFieldList);
		((DefaultComparator)comparator).setIsDescending();
		ArrayList tmpList = list;
		this.clear();
		Collections.sort(tmpList, comparator);
		for (int i = 0; i < tmpList.size(); i++) {
			this.add(tmpList.get(i));
		}
	}

	/**
	 * 20100812 dashontsai added for 傳入Sort型態
	 */
	public void sort(String i_sorttype) {
		Comparator comparator = null;
		if (SORT_TYPE_DEFAULT.equals(i_sorttype)) {
			comparator = new DefaultComparator(this.sortFieldList);
		} else if (SORT_TYPE_LENGTH.equals(i_sorttype)) {
			comparator = new LengthComparator(this.sortFieldList);
		} else {
			throw new RuntimeException("SortType not match....");
		}
		ArrayList tmpList = list;
		this.clear();
		Collections.sort(tmpList, comparator);
		for (int i = 0; i < tmpList.size(); i++) {
			this.add(tmpList.get(i));
		}
	}

	/**
	 * 20100812 dashontsai added for 傳入Sort型態
	 */
	public void sortDesc(String i_sorttype) {
		Comparator comparator = null;
		if (SORT_TYPE_DEFAULT.equals(i_sorttype)) {
			comparator = new DefaultComparator(this.sortFieldList);
			((DefaultComparator)comparator).setIsDescending();
		} else if (SORT_TYPE_LENGTH.equals(i_sorttype)) {
			comparator = new LengthComparator(this.sortFieldList);
			((LengthComparator)comparator).setIsDescending();
		} else {
			throw new RuntimeException("SortType not match....");
		}
		ArrayList tmpList = list;
		this.clear();
		Collections.sort(tmpList, comparator);
		for (int i = 0; i < tmpList.size(); i++) {
			this.add(tmpList.get(i));
		}
	}
	
	/**
	 * 20100812 dashontsai added for 傳入Sort型態
	 */
	public void sortIgnoreEmpty(String i_sorttype) {
		Comparator comparator = null;
		if (SORT_TYPE_DEFAULT.equals(i_sorttype)) {
			comparator = new DefaultComparator(this.sortFieldList);
			((DefaultComparator)comparator).setIgnoreEmptyField();
		} else if (SORT_TYPE_LENGTH.equals(i_sorttype)) {
			comparator = new LengthComparator(this.sortFieldList);
			((LengthComparator)comparator).setIgnoreEmptyField();
		} else {
			throw new RuntimeException("SortType not match....");
		}
		ArrayList tmpList = list;
		this.clear();
		Collections.sort(tmpList, comparator);
		for (int i = 0; i < tmpList.size(); i++) {
			this.add(tmpList.get(i));
		}
	}

	/**
	 * 20100812 dashontsai added for 傳入Sort型態
	 */
	public void sortDescIgnoreEmpty(String i_sorttype) {
		Comparator comparator = null;
		if (SORT_TYPE_DEFAULT.equals(i_sorttype)) {
			comparator = new DefaultComparator(this.sortFieldList);
			((DefaultComparator)comparator).setIsDescending();
			((DefaultComparator)comparator).setIgnoreEmptyField();
		} else if (SORT_TYPE_LENGTH.equals(i_sorttype)) {
			comparator = new LengthComparator(this.sortFieldList);
			((LengthComparator)comparator).setIsDescending();
			((LengthComparator)comparator).setIgnoreEmptyField();
		} else {
			throw new RuntimeException("SortType not match....");
		}
		ArrayList tmpList = list;
		this.clear();
		Collections.sort(tmpList, comparator);
		for (int i = 0; i < tmpList.size(); i++) {
			this.add(tmpList.get(i));
		}
	}

	/* no longer use
	public int compare(Object o1, Object o2) {
		DataAccessor obj1 = (DataAccessor) o1;
		DataAccessor obj2 = (DataAccessor) o2;

		for (int i = 0; i < this.sortFieldList.size(); i++) {
			String strFieldname = (String) this.sortFieldList.get(i);
			String strCompStr1 = obj1.getValue(strFieldname.toLowerCase());
			String strCompStr2 = obj2.getValue(strFieldname.toLowerCase());

			if (strCompStr1.compareTo(strCompStr2) == 0) {
				continue;
			} else {
				return strCompStr1.compareTo(strCompStr2);
			}
		}
		return 0;
	}*/

	/**
	 * 設定排序欄位,設定後可使用sort(),sortDesc()排序
	 */
	public void setSortField(String[] iFields) {
		this.sortFieldList = new ArrayList();
		for (int i = 0; i < iFields.length; i++) {
			this.sortFieldList.add(iFields[i].toLowerCase());
		}
	}

	/**
	 * 設定排序欄位,設定後可使用sort(),sortDesc()排序
	 */
	public void setSortField(String iFields) {
		setSortField(iFields.split(","));
	}

	//#########################
	/**
	 * 20100812 dashontsai added
	 * 一般文字Sort
	 * 1. a
	 * 2. aa
	 * 3. b
	 */
	public class DefaultComparator implements Comparator {
		protected ArrayList sortFieldList = new ArrayList();
		private boolean ignoreEmptyField = false;
		private int intDescendingControll = 1;

		public DefaultComparator(ArrayList sortFieldList) {
			this.sortFieldList = sortFieldList;
		}

		public void setIsDescending(){
			intDescendingControll = -1;
		}

		public void setIgnoreEmptyField(){
			ignoreEmptyField = true;
		}

		public int compare(Object o1, Object o2) {
			DataAccessor obj1 = (DataAccessor) o1;
			DataAccessor obj2 = (DataAccessor) o2;
			int intReturn = 0;

			for (int i = 0; i < this.sortFieldList.size(); i++) {
				String strFieldname = (String) this.sortFieldList.get(i);
				String strCompStr1 = obj1.getValue(strFieldname.toLowerCase());
				String strCompStr2 = obj2.getValue(strFieldname.toLowerCase());

				if (this.ignoreEmptyField) {
					if (strCompStr1.trim().length() == 0) {
						intReturn = 1;
						break;
					}
					if (strCompStr2.trim().length() == 0) {
						intReturn = -1;
						break;
					}
				}

				if (strCompStr1.compareTo(strCompStr2) == 0) {
					continue;
				} else {
					intReturn = strCompStr1.compareTo(strCompStr2);
					intReturn = intReturn *intDescendingControll;
					break;
				}
			}
			
			return intReturn;
		}
	}

	/**
	 * 20100812 dashontsai added
	 * 文字長度Sort
	 * 1. a
	 * 2. c
	 * 3. aa
	 */
	public class LengthComparator implements Comparator {
		protected ArrayList sortFieldList = new ArrayList();
		private boolean ignoreEmptyField = false;
		private int intDescendingControll = 1;

		public LengthComparator(ArrayList sortFieldList) {
			this.sortFieldList = sortFieldList;
		}

		public void setIsDescending(){
			intDescendingControll = -1;
		}

		public void setIgnoreEmptyField(){
			ignoreEmptyField = true;
		}

		public int compare(Object o1, Object o2) {
			DataAccessor obj1 = (DataAccessor) o1;
			DataAccessor obj2 = (DataAccessor) o2;
			int intReturn = 0;

			for (int i = 0; i < this.sortFieldList.size(); i++) {
				String strFieldname = (String) this.sortFieldList.get(i);
				String strCompStr1 = obj1.getValue(strFieldname.toLowerCase());
				String strCompStr2 = obj2.getValue(strFieldname.toLowerCase());

				if (this.ignoreEmptyField) {
					if (strCompStr1.trim().length() == 0) {
						intReturn = 1;
						break;
					}
					if (strCompStr2.trim().length() == 0) {
						intReturn = -1;
						break;
					}
				}

				if (strCompStr1.length() > strCompStr2.length()) {
					intReturn = 1;
					intReturn = intReturn * intDescendingControll;
					break;
				} else if (strCompStr1.length() < strCompStr2.length()) {
					intReturn = -1;
					intReturn = intReturn * intDescendingControll;
					break;
				} else if (strCompStr1.compareTo(strCompStr2) == 0) {
					continue;
				} else {
					intReturn = strCompStr1.compareTo(strCompStr2);
					intReturn = intReturn * intDescendingControll;
					break;
				}
			}
			
			return intReturn;
		}
	}
}
