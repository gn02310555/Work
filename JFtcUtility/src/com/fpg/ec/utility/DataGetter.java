package com.fpg.ec.utility;
import java.util.ArrayList;
import java.util.Hashtable;


public class DataGetter {
	private Hashtable hstData;
	private ArrayList alList;
	public DataGetter() {
	}

	public DataGetter(Hashtable i_data) {
		this.hstData = i_data;
	}

	public DataGetter(ArrayList i_list) {
		this.alList = i_list;
	}

	public DataGetter(Hashtable i_data, ArrayList i_list) {
		this.hstData = i_data;
		this.alList = i_list;
	}

	public String getData(String i_fieldname) {
		String strReturn = (String) hstData.get(i_fieldname);
		if (strReturn == null)
			return "";
		return strReturn;
	}

	public String getListData(String i_fieldname, int i_inx) {
		//Hashtable hstTmp = (Hashtable) alList.get(i_inx);
		String strReturn = "";
		Object obj = alList.get(i_inx);

		if (obj == null)
			return "";

		if (obj instanceof Hashtable) {
			Hashtable hstTmp = (Hashtable) obj;
			strReturn = (String) hstTmp.get(i_fieldname);
			if (strReturn == null)
				return "";
		}


		return strReturn;
	}

	public int getListSize() {
		if (alList == null)
			return -1;
		return alList.size();
	}

	public Hashtable getAllData() {
		return hstData;
	}

	public Hashtable getAllData(int i_inx) {
		return (Hashtable) alList.get(i_inx);
	}

	public ArrayList getAllListData() {
		return alList;
	}

}