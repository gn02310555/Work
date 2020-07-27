package com.fpg.ec.group.model.list;

import java.util.Hashtable;

import com.fpg.ec.group.model.Dp;
import com.fpg.ec.utility.db.DataAccessor;
import com.fpg.ec.utility.db.SKDataAccessorList;

/**
 * DpModel List物件(Dp)
 * @author Jason
 */
public class DpList extends SKDataAccessorList {
   
    private static final long serialVersionUID = 7493844310496215597L;
   
    
   
	public DataAccessor genObj(Hashtable i_data){
		return new Dp(i_data);
	}

    public Dp getDp(String i_key) {
        return (Dp) super.get(i_key);
    }

    public Dp getDp(int i_index) {
        return (Dp) super.get(i_index);
    }	
	
}

