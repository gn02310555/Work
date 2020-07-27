package com.fpg.ec.utility.db;

import java.util.Hashtable;

import com.fpg.ec.utility.StringUtil;

/**
 * SingleKey DataAccessorList
 * @author Evan
 *
 */
public abstract class SKDataAccessorList extends DataAccessorList {
	private static final long serialVersionUID = 2744573960937683650L;
	

	public final String getKey(Object i_obj) {
		if (i_obj instanceof Hashtable) {
			i_obj = genObj((Hashtable) i_obj);
		}
		SKDataAccessor obj = ((SKDataAccessor) i_obj);
		if (obj.getXuid().trim().length() == 0) {
			String strNewXuid = "";
			do { 
				strNewXuid = StringUtil.genXUID();
			} while (containsKey(strNewXuid));//避免瞬間取得相同值
			obj.setXuid(strNewXuid);
		}
		return obj.getXuid();
	}
	
	/*
	public void regenXuid(){
		HashMap hmXuidMap = new HashMap();
		for( int i=0;i<this.size();i++){
			SKDataAccessor sd = (SKDataAccessor)this.get(i);
			String strNewXuid = "";
			do { 
				strNewXuid = StringUtil.genXUID();
			} while (hmXuidMap.containsKey(strNewXuid));//避免瞬間取得相同值
			sd.setXuid(strNewXuid);
		}
	}*/
	

	public DataAccessor getObject(String i_key) {
		return (DataAccessor) super.get(i_key);
	}

	public DataAccessor getObject(int i_index) {
		return (DataAccessor) super.get(i_index);
	}
	

	public abstract DataAccessor genObj(Hashtable i_data);

}
