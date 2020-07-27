/*
 * @(#)CollectionUtil.java	1.00 02/12/09
 *
 */

package com.fpg.ec.utility;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 此類別提供Collection framework物件一些額外的工具
 * @author  pc27/Patterson win
 * @version 1.00 02/12/09  
 */
public class CollectionUtil {

	/**
	 * 將Hashtable's key or value 轉換成ArrayList
	 * @param Hashtable  i_hst : 欲轉換之Hashtable
	 * @param int i_type : 類別{"key","value"} (轉出Hashtable's key or value)
	 * @return ArrayList
	 */
	public ArrayList toArrayList(Hashtable i_hst, String i_type) {
		ArrayList alReturn = new ArrayList();
		if (i_hst != null && !i_hst.isEmpty()) {
			Enumeration enumTemp = i_hst.keys();
			if (i_type.equals("key")) {
				for (; enumTemp.hasMoreElements();) {
					String key = (String) enumTemp.nextElement();
					alReturn.add(key);
				}
			} else
				if (i_type.equals("value")) {
					for (; enumTemp.hasMoreElements();) {
						String key = (String) enumTemp.nextElement();
						Object value = (Object) i_hst.get(key);
						alReturn.add(value);
					}
				}
		}
		return alReturn;
	}


	/**
	 * 將ArrayList 轉換成 Hashtable's key or value
	 * @param ArrayList  i_al : 欲轉換之ArrayList
	 * @param int i_type : 類別{"key","value"} (轉入至Hashtable's key or value)
	 * @return ArrayList
	 */
	public Hashtable toHashtable(ArrayList i_al, String i_type) {
		Hashtable hstReturn = new Hashtable();
		if (i_al != null && !i_al.isEmpty()) {
			int alSize = i_al.size();
			if (i_type.equals("key")) {
				for (int i = 0; i < alSize; i++) {
					hstReturn.put(new Integer(i).toString(), i_al.get(i));
				}
			} else
				if (i_type.equals("value")) {
					for (int i = 0; i < alSize; i++) {
						hstReturn.put(i_al.get(i), "Y");
					}
				}

		}
		return hstReturn;
	}

}